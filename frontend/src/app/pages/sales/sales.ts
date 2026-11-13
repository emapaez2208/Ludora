import { Component, effect, inject, signal, untracked } from '@angular/core';
import { SaleService } from '../../services/sale-service';
import { Pagination } from '../../components/pagination/pagination';
import { UserStateService } from '../../services/user-state-service';
import { ClientResponse } from '../../models/client-response';
import { SaleResponse } from '../../models/sale-response';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  imports: [Pagination, CommonModule, RouterLink],
  selector: 'app-sales',
  styleUrl: './sales.css',
  templateUrl: './sales.html',
})
export class Sales {

  private saleService = inject(SaleService)
  private userState = inject(UserStateService)

  sales = signal<SaleResponse[]>([])
  client = signal<ClientResponse | null>(null)

  isLoading = signal(true)
  msjError = signal('')

  page = signal(0)
  size = signal(18)
  totalPages = signal(0)


  constructor(){
    effect(() => {
      if(this.userState.client()){
        untracked(() => {
          this.client.set(this.userState.client())
          this.page.set(0)
          this.loadSales()
        })
      }
    })
  }

  loadSales(){

    const client = this.client()

    if(!client){
      this.isLoading.set(false)
      return
    }

    this.isLoading.set(true)
    this.msjError.set('')

    this.saleService.getSalesByClient(this.page(), this.size(), client.externalId)
      .subscribe({
        next: data => {
          this.sales.set(data.content)
          this.totalPages.set(data.totalPages)
          this.isLoading.set(false)
        },
        error: e => {
          console.error('Error al cargar las sales: ', e)
          this.msjError.set('No se pudieron cargar tus compras.')
          this.isLoading.set(false)
        }
      })
  }

  goToPage(page: number) {
    this.page.set(page);
    this.loadSales();
  }
}
