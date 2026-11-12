import { Component, computed, ElementRef, HostListener, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { CartService } from '../../services/cart-service';
import { ToastService } from '../../services/toast-service';
import { UserStateService } from '../../services/user-state-service';

@Component({
  imports: [RouterLink],
  selector: 'app-header',
  styleUrl: './header.css',
  templateUrl: './header.html',
})
export class Header {
  isMenuOpen: boolean = false;
  authService = inject(AuthService);
  userStateService = inject(UserStateService);
  router = inject(Router);
  cartService = inject(CartService);
  toastService = inject(ToastService);

  private el = inject(ElementRef);

  isLogged = this.authService.isLoggedIn$;
  cartOpen = signal(false);
  adminOpen = signal(false);
  developerOpen = signal(false);

  userAvatar = computed(() => {
    const user =
      this.userStateService.client() ||
      this.userStateService.developer() ||
      this.userStateService.admin();

    return user?.avatarUrl || 'images/profile-base.jpeg';
  });

  userName = computed(() => {
    const user =
      this.userStateService.client() ||
      this.userStateService.developer() ||
      this.userStateService.admin();

    return user?.userName || this.authService.getUsername() || 'Usuario';
  });

  isClient(): boolean {
    return this.authService.isClient();
  }

  isDeveloper(): boolean {
    return this.authService.isDeveloper();
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  toggleCart(): void {
    this.cartOpen.update(v => !v);
  }

  toggleAdmin(event: MouseEvent): void {
    event.stopPropagation();
    this.cartOpen.set(false);
    this.developerOpen.set(false);
    this.adminOpen.update(v => !v);
  }

  toggleDeveloper(event: MouseEvent): void {
    event.stopPropagation();
    this.cartOpen.set(false);
    this.adminOpen.set(false);
    this.developerOpen.update(v => !v);
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

  @HostListener('document:click', ['$event'])
  onDocClick(e: MouseEvent): void {

    this.adminOpen.set(false);
    this.developerOpen.set(false);

    if (!this.el.nativeElement.contains(e.target)) {
      this.cartOpen.set(false);
    }
    
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(): void {

    this.isMenuOpen = false;
    this.adminOpen.set(false);
    this.developerOpen.set(false);

  }

}