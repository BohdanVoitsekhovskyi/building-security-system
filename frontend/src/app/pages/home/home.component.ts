import { Component } from '@angular/core';
import { NavbarComponent } from "../../utils/navbar/navbar.component";
import { BuildingSchemaComponent } from "../../utils/building-schema/building-schema.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, BuildingSchemaComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  
}
