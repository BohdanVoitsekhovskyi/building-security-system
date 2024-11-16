import { Component, inject } from '@angular/core';
import { NavbarComponent } from "../../utils/navbar/navbar.component";
import { BuildingSchemaComponent } from "../../utils/building-schema/building-schema.component";
import { PopupService } from '../../utils/info-popup/popup.service';
import { PopupInfo } from '../../utils/info-popup/popup.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, BuildingSchemaComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
}
