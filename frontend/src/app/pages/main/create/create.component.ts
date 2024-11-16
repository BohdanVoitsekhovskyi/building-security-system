import { Component } from '@angular/core';
import { NavbarComponent } from '../../../utils/navbar/navbar.component';
import { floors} from './dummy-data';
import { CommonModule } from '@angular/common';
import { BuildingSchemaComponent } from "../../../utils/building-schema/building-schema.component";

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [NavbarComponent, CommonModule, BuildingSchemaComponent],
  templateUrl: './create.component.html',
  styleUrl: './create.component.css',
})
export class CreateComponent {
  floors = floors;
  activeFloor?: number;
  state: 'creating' | 'modifying' | 'saved' = 'saved';

  changeFloor(event: Event) {
    const element = event.target as HTMLDivElement;
    this.activeFloor = +element.id;
    if (this.state === 'creating') {
      floors.pop();
      this.state = 'saved';
    }
    //change floor svg
  }

  addFloor() {
    if (this.state !== 'saved') return;

    floors.push({ id: (floors.at(-1)?.id ?? 0) + 1, data: null });
    this.activeFloor = floors.at(-1)?.id ?? this.activeFloor;
    this.state = 'creating';
  }

  saveFloor() {
    this.state = 'saved';
  }

  onFileUpload(event: Event) {
    const file = (event.target as HTMLInputElement).files?.item(0);
    this.state = 'modifying';
  }
}
