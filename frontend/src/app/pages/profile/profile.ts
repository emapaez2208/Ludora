import { Component, effect, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { UserStateService } from '../../services/user-state-service';
import { SaleService } from '../../services/sale-service';
import { SaleResponse } from '../../models/sale-response';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  imports: [RouterLink, DatePipe],
  selector: 'app-profile',
  styleUrl: './profile.css',
  templateUrl: './profile.html',
})
export class Profile {

  userStateService = inject(UserStateService);
  
  private authService = inject(AuthService);
  private router = inject(Router);
  private saleService = inject(SaleService);

  recentSales = signal<SaleResponse[]>([]);
  salesLoading = signal(false);
  salesError = signal(false);

  logout(): void {

    this.authService.logout();
    this.router.navigate(['/']);

  }

  constructor() {
    effect(() => {

      const client = this.userStateService.client();

      if (!client) {
        this.recentSales.set([]);
        return;
      }

      this.salesLoading.set(true);
      this.salesError.set(false);

      this.saleService.getSalesByClient(0, 3, client.externalId).subscribe({
        next: data => {
          this.recentSales.set(data.content);
          this.salesLoading.set(false);
        },
        error: error => {
          console.error('Error al cargar las compras recientes:', error);
          this.salesError.set(true);
          this.salesLoading.set(false);
        }
      });
    });
  }
}