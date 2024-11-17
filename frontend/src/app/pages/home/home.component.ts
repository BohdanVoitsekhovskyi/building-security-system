import { Component, computed, inject } from '@angular/core';
import { NavbarComponent } from "../../utils/navbar/navbar.component";
import { BuildingSchemaComponent } from "../../utils/building-schema/building-schema.component";
import { CommonModule } from '@angular/common';
import { FacilityService } from '../../services/facility.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, BuildingSchemaComponent, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  private facilityService = inject(FacilityService);

  floors = computed(() => this.facilityService.facility()?.floors); 
  activeFloor: number = 1;

  changeFloor(event: Event) {
    const element = event.target as HTMLDivElement;
    this.activeFloor = +element.id;
    element.classList.toggle('selected');
    //change floor svg
  }
}
