import { Component, computed, inject } from '@angular/core';
import { NavbarComponent } from '../../../utils/navbar/navbar.component';
import { BuildingSchemaComponent } from '../../../utils/building-schema/building-schema.component';
import { FacilityService } from '../../../services/facility.service';
import { LoggerComponent } from '../../../utils/logger/logger.component';
import { CommonModule } from '@angular/common';
import { TesterService } from '../../../services/tester.service';

@Component({
  selector: 'app-test',
  standalone: true,
  imports: [
    NavbarComponent,
    BuildingSchemaComponent,
    LoggerComponent,
    CommonModule,
  ],
  templateUrl: './test.component.html',
  styleUrl: './test.component.css',
})
export class TestComponent {
  private facilityService = inject(FacilityService);
  private testerService = inject(TesterService);

  floors = computed(() => this.facilityService.facility()?.floors);
  activeFloor?: number = 1;
  pingFloors = computed(() =>
    this.floors()
      ?.filter((f) =>
        f.detectors.some((d) =>
          this.testerService
            .systemReaction()
            .map((sr) => sr.detectorsReaction.map((dr) => dr.detector.id))
            .reduce((arr, curr) => [...arr, ...curr], [])
            .some((p) => p === d.id)
        )
      )
      .map((f) => f.floorNumber)
  );

  changeFloor(event: Event) {
    const element = event.target as HTMLDivElement;
    if (this.activeFloor === +element.id) return;
    this.activeFloor = +element.id;
    element.classList.toggle('selected');
  }

  floorPinged(floorNumber: number) {
    return !!this.pingFloors()?.find((f) => f === floorNumber);
  }
}
