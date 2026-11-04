import { Component, ElementRef, HostListener, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { CartService } from '../../services/cart-service';

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
  cartService = inject(CartService);

  private el = inject(ElementRef);

  isLogged = this.authService.isLoggedIn$;
  cartOpen = signal(false);

  isClient(): boolean {
    return this.authService.isClient();
  }

  toggleCart(): void {
    this.cartOpen.update(v => !v);
  }

  // cierra el desplegable del carrito al hacer click afuera del header
  @HostListener('document:click', ['$event'])
  onDocClick(e: MouseEvent): void {
    if (!this.el.nativeElement.contains(e.target)) {
      this.cartOpen.set(false);
    }
  }

  logout() {
    this.closeMenu();
    this.cartOpen.set(false);
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
