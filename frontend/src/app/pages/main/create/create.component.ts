import { Component, computed, inject, signal } from '@angular/core';
import { NavbarComponent } from '../../../utils/navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { BuildingSchemaComponent } from '../../../utils/building-schema/building-schema.component';
import { FacilityService } from '../../../services/facility.service';
import { Floor } from '../../../models/floor.model';
import { Detector } from '../../../models/detector.model';

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [NavbarComponent, CommonModule, BuildingSchemaComponent],

  templateUrl: './create.component.html',
  styleUrl: './create.component.css',
})
export class CreateComponent {
  private facilityService = inject(FacilityService);
  floors = computed(() => this.facilityService.facility()?.floors);
  newFloor?: { floorNumber: number; file: string };
  activeFloor?: number;
  state: 'creating' | 'modifying' | 'saved' = 'saved';
  svgUrl?: string;

  detectors: Detector[] = [];

  changeFloor(event: Event) {
    const element = event.target as HTMLDivElement;
    this.activeFloor = +element.id;
    if (this.state === 'creating' || this.state === 'modifying') {
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
          },
        });
    }

    this.newFloor = undefined;
    this.state = 'saved';
  }

  ngOnDestroy(): void {
    if (this.svgUrl) {
      URL.revokeObjectURL(this.svgUrl);
    }
  }
}
