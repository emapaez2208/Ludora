import { HttpClient } from '@angular/common/http';
import { inject, Service, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';

import { AuthService } from './auth-service';
import { Cart } from '../models/cart';

@Service()
export class CartService {

  private http = inject(HttpClient);
  private authService = inject(AuthService);

  private url = '/api/clients/cart';

  readonly cart = signal<Cart | null>(null);

  //carga de carrito 

  load(): Observable<Cart> {
    return this.http.get<Cart>(this.url).pipe(
      tap(cart => this.cart.set(cart))
    );
  }

  // quitar juego del carito

  remove(gameId: string): Observable<Cart> {
    const clientId = this.authService.getClientExternalId();
    return this.http.delete<Cart>(`${this.url}/${clientId}/games/${gameId}`).pipe(
      tap(cart => this.cart.set(cart))
    );
  }
}