import { Component, computed, inject, signal } from '@angular/core';
import { NavbarComponent } from '../../../utils/navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { BuildingSchemaComponent } from '../../../utils/building-schema/building-schema.component';
import { FacilityService } from '../../../services/facility.service';
import { Floor } from '../../../models/floor.model';
import { Detector } from '../../../models/detector.model';
import { PopupService } from '../../../utils/info-popup/popup.service';
import { PopupInfo } from '../../../utils/info-popup/popup.model';
import { LoadSpinnerAltComponent } from "../../../shared/load-spinner-alt/load-spinner-alt.component";

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [NavbarComponent, CommonModule, BuildingSchemaComponent, LoadSpinnerAltComponent],

  templateUrl: './create.component.html',
  styleUrl: './create.component.css',
})
export class CreateComponent {
  private popupService = inject(PopupService);
  private facilityService = inject(FacilityService);
  floors = computed(() => this.facilityService.facility()?.floors);
  activeFloor?: number = 1;
  newFloor?: { floorNumber: number; file: string };
  state: 'creating' | 'modifying' | 'placing' | 'saved' = 'saved';
  svgUrl?: string;
  detectors?: Detector[];

  changeFloor(event: Event) {
    const element = event.target as HTMLDivElement;
    this.activeFloor = +element.id;
    if (this.state !== 'saved') {
      this.newFloor = undefined;
      this.state = 'saved';
    }
  }

  addFloor() {
    if (this.state !== 'saved') return;

    this.newFloor = {
      floorNumber: (this.floors()?.length || 0) + 1,
      file: '',
    };
    this.activeFloor = this.newFloor.floorNumber;
    this.state = 'creating';
  }

  onFileUpload(event: Event) {
    if (!this.newFloor) {
      console.log('wtf');
      return;
    }

    const input = event.target as HTMLInputElement;
    let fileContent = '';

    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      if (file.type === 'image/svg+xml') {
        this.svgUrl = URL.createObjectURL(file); // Create an object URL
      } else {
        console.error('Selected file is not an SVG');
      }

      const reader = new FileReader();

      reader.onload = (e) => {
        fileContent = e.target?.result as string;
        if (this.newFloor) this.newFloor.file = fileContent;
      };

      reader.onerror = (e) => {
        console.error('Error reading file:', e);
      };

      reader.readAsText(file);
    }

    this.state = 'modifying';
  }

  onSaveFloor() {
    if (this.newFloor?.floorNumber && this.newFloor.file) {
      this.facilityService
        .addFloor(this.newFloor?.floorNumber, this.newFloor.file)
        .subscribe({
          next: (res) => {
            this.facilityService.facility.set(res);
            console.log(res);
          },
          error: (err) => {
            console.log(err);
            this.showPopup('fail');
            return;
          },
        });
      this.newFloor = undefined;
    } else if (this.detectors && this.activeFloor) {
      this.facilityService
        .addDetectors(this.activeFloor, this.detectors)
        .subscribe({
          next: (res) => {
            this.facilityService.facility.set(res);
            console.log(res);
          },
          error: (err) => {
            console.log(err);
            this.showPopup('fail');
            return;
          },
        });
    }

    this.state = 'saved';
    this.showPopup('success');
  }

  onAddSensor(detectors: Detector[]) {
    this.detectors = detectors;
    this.state = 'placing';
  }

  removeFloor(floor: Floor) {
    //remove floor
  }

  ngOnDestroy(): void {
    if (this.svgUrl) {
      URL.revokeObjectURL(this.svgUrl);
    }
  }

  showPopup(type: 'success' | 'fail') {
    let info: PopupInfo;
    if (type === 'success'){
      info = {
        name: 'Success',
        description: 'Floor was successfully saved!',
        type: type
      };
    }
    else {
      info = {
        name: 'Unexpected',
        description: 'Unexpected something happened. Never do that again, please',
        type: 'error'
      };
    }

    this.popupService.showPopup(info);
  }
}
