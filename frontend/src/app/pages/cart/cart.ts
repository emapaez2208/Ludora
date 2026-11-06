import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../../services/cart-service';
import { SaleService } from '../../services/sale-service';
import { SaleRequest } from '../../models/sale-request';

@Component({
  imports: [RouterLink],
  selector: 'app-cart',
  styleUrl: './cart.css',
  templateUrl: './cart.html',
})
export class Cart implements OnInit {

  private cartService = inject(CartService);
  private router = inject(Router)
  private saleService = inject(SaleService)

  cart = this.cartService.cart;
  games = computed(() => this.cart()?.games ?? []);

  isLoading = signal(true);
  errorMsg = signal('');
  removingId = signal<string | null>(null);

  // Subtotal con precios de lista y ahorro (si el cliente alcanzó el umbral de puntos)
  subtotal = computed(() => this.games().reduce((acc, g) => acc + g.price, 0));
  total = computed(() => this.cart()?.totalPrice ?? 0);
  savings = computed(() => Math.max(this.subtotal() - this.total(), 0));
  hasDiscount = computed(() => this.games().some(g => g.discountedPrice !== null));

  ngOnInit(): void {
    this.cartService.load().subscribe({
      next: () => this.isLoading.set(false),
      error: (err) => {
        console.error('Error al cargar el carrito', err);
        this.errorMsg.set('No pudimos cargar tu carrito. Intentá de nuevo en unos minutos.');
        this.isLoading.set(false);
      }
    });
  }

  remove(gameId: string) {
    this.errorMsg.set('');
    this.removingId.set(gameId);
    this.cartService.remove(gameId).subscribe({
      next: () => this.removingId.set(null),
      error: (err) => {
        console.error('Error al quitar el juego', err);
        this.errorMsg.set(err.error?.message ?? 'No pudimos quitar el juego del carrito.');
        this.removingId.set(null);
      }
    });
  }

  payCart(){
    if(this.games().length === 0 && !this.cart()) return

    const saleRequest : SaleRequest = {clientExternalId : this.cart()!.client.externalId}

    this.saleService.createSale(saleRequest).subscribe({
      next: data => {
        this.router.navigate(['/sales', data.externalId])
      },
      error: e => {
        console.error('Error al crear la Sale: ', e)
      }
    })
  }
}
