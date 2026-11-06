import { Component, inject, signal } from '@angular/core';
import { SaleService } from '../../services/sale-service';
import { ActivatedRoute } from '@angular/router';
import { SaleResponse } from '../../models/sale-response';

@Component({
  imports: [],
  selector: 'app-sales-details',
  styleUrl: './sales-details.css',
  templateUrl: './sales-details.html',
})
export class SalesDetails {

  private saleService = inject(SaleService)
  private route = inject(ActivatedRoute)

  sale = signal<SaleResponse | null>(null)


  constructor(){
    this.loadSale()
  }

  loadSale(){
    const id = this.route.snapshot.params['id']
    this.saleService.getSale(id).subscribe({
      next: data => this.sale.set(data),
      error: e => {
        console.error('Erorr al cargar la sale: ', e)
      }
    })
  }
}
