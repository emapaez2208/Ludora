import { Component, inject, signal } from '@angular/core';
import { SaleService } from '../../services/sale-service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { SaleResponse } from '../../models/sale-response';
import { CommonModule } from '@angular/common';

@Component({
  imports: [CommonModule, RouterLink],
  selector: 'app-sales-details',
  styleUrl: './sales-details.css',
  templateUrl: './sales-details.html',
})
export class SalesDetails {

  private saleService = inject(SaleService)
  private route = inject(ActivatedRoute)

  sale = signal<SaleResponse | null>(null)
  isPaying = signal(false)
  msjError = signal('')


  constructor(){
    this.loadSale()
  }

  loadSale(){
    const id = this.route.snapshot.params['id']
    
    this.saleService.getSale(id).subscribe({
      next: data => this.sale.set(data),
      error: e => {
        console.error('Erorr al cargar la sale: ', e)
        this.msjError.set('La compra a la que quiere acceder no existe..')
      }
    })
  }

  paySale(){

    if(!this.sale() || this.sale()?.status === 'APPROVED') return

    this.isPaying.set(true)

    this.saleService.paySale(this.sale()!.externalId).subscribe({
      next: url => {
        if(url){
          window.location.href = url
        }
        this.isPaying.set(false)
      },
      error: e => {
        console.error('Error al generar el pago: ', e)
        this.isPaying.set(false)
      }
    })
  }
}
