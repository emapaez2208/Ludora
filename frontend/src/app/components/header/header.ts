import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  imports: [RouterLink],
  selector: 'app-header',
  styleUrl: './header.css',
  templateUrl: './header.html',
})
export class Header {
  isMenuOpen: boolean = false;
  authService = inject(AuthService)
  router = inject(Router)

  isLogged = this.authService.isLoggedIn$;

  logout() {
    this.closeMenu()
    this.authService.logout();
    this.router.navigate(['/home']);
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(): void {
    this.isMenuOpen = false;
  }

  
}
