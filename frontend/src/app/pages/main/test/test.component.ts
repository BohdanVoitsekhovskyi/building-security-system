import { Component, computed, inject } from '@angular/core';
import { NavbarComponent } from "../../../utils/navbar/navbar.component";
import { BuildingSchemaComponent } from "../../../utils/building-schema/building-schema.component";
import { FacilityService } from '../../../services/facility.service';
import { LoggerComponent } from "../../../utils/logger/logger.component";

@Component({
  selector: 'app-test',
  standalone: true,
  imports: [NavbarComponent, BuildingSchemaComponent, LoggerComponent],
  templateUrl: './test.component.html',
  styleUrl: './test.component.css'
})
export class TestComponent {
  private facilityService = inject(FacilityService);
  floors = computed(() => this.facilityService.facility()?.floors);
  activeFloor?: number = 1;
}
