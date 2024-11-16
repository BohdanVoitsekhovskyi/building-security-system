import { Component, Host, HostBinding } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  @HostBinding('class.hidden') isHidden = false;

  onHide() {
    this.isHidden = !this.isHidden;
  }
}
