import { HttpClient } from '@angular/common/http';
import { computed, effect, inject, Service, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';

import { AuthService } from './auth-service';
import { Cart } from '../models/cart';
import { ToastService } from './toast-service';
import { Router } from '@angular/router';

@Service()
export class CartService {

  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private toast = inject(ToastService);
  private router = inject(Router)

  private url = '/api/clients/cart';

  readonly cart = signal<Cart | null>(null);
  readonly count = computed(() => this.cart()?.games.length ?? 0);
  readonly total = computed(() => this.cart()?.totalPrice ?? 0);

  constructor() {
    effect(() => {
      const logged = this.authService.isLoggedIn$();
      if (logged && this.authService.isClient()) {
        this.load().subscribe({ error: () => {} });
      } else {
        this.cart.set(null);
      }
    });
  }

  
  //carga de carrito 

  load(): Observable<Cart> {
    return this.http.get<Cart>(this.url).pipe(
      tap(cart => this.cart.set(cart))
    );
  }

  //agregar juego al carrito

  add(game: { externalId: string; name: string }) {
    if (!this.authService.isLoggedIn()) {
      this.askLogin();
      return;
    }

    if (!this.authService.isClient()) {
      this.toast.show('Solo los clientes pueden comprar juegos', 'info');
      return;
    }

    const clientId = this.authService.getClientExternalId();

    this.http.post<Cart>(`${this.url}/${clientId}/games/${game.externalId}`, null).subscribe({
      next: (cart) => {
        this.cart.set(cart);
        this.toast.show(`"${game.name}" se agregó al carrito. Tocá para verlo`, 'success', 4000, '/cart');
      },
      error: (err) => {
        if (err.status === 409) {
          this.toast.show('Ese juego ya está en tu carrito o ya lo tenés', 'info');
        } else if (err.status === 401 || err.status === 403) {
          this.askLogin();
        } else {
          this.toast.show('No se pudo agregar al carrito', 'error');
        }
      },
    });
  }

  // quitar juego del carito

  remove(gameId: string): Observable<Cart> {
    const clientId = this.authService.getClientExternalId();
    return this.http.delete<Cart>(`${this.url}/${clientId}/games/${gameId}`).pipe(
      tap(cart => this.cart.set(cart))
    );
  }


  private askLogin() {
    this.toast.show('Iniciá sesión para agregar juegos al carrito', 'info');
    this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
  }

  has(gameId: string): boolean {
    return this.cart()?.games.some(g => g.externalId === gameId) ?? false;
  }
}