import { Component } from '@angular/core';
import { NavbarComponent } from "../../utils/navbar/navbar.component";
import { BuildingSchemaComponent } from "../../utils/building-schema/building-schema.component";
import { CommonModule } from '@angular/common';
import { floors } from './dummy-data';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, BuildingSchemaComponent, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  floors = floors;
  activeFloor: number = 1;

  changeFloor(event: Event) {
    const element = event.target as HTMLDivElement;
    this.activeFloor = +element.id;
    element.classList.toggle('selected');
    //change floor svg
  }
}
