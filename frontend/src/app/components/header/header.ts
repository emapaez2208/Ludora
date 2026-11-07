import { Component, ElementRef, HostListener, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { CartService } from '../../services/cart-service';
import { ToastService } from '../../services/toast-service';

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
  toastService = inject(ToastService);

  private el = inject(ElementRef);

  isLogged = this.authService.isLoggedIn$;
  cartOpen = signal(false);

  isClient(): boolean {
    return this.authService.isClient();
  }

  toggleCart(): void {
    this.cartOpen.update(v => !v);
  }

  onRemoveGame(game: { externalId: string; name: string }, event: MouseEvent) {
  event.stopPropagation();

  this.cartService.remove(game.externalId).subscribe({
    next: () => {
      this.toastService.show(
        `Se eliminó "${game.name}" del carrito.`, 
        'info', 
        5000, 
        undefined, 
        {
          label: 'Deshacer',
          fn: () => {
            this.cartService.add(game);
          }
        }
      );
    },
    error: () => {
      this.toastService.show('No se pudo eliminar el juego', 'error');
    }
  });
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
