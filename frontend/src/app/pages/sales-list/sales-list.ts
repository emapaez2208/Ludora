import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SaleService } from '../../services/sale-service';
import { SaleResponse } from '../../models/sale-response';
import { SaleFilters } from '../../models/sale-filters';
import { Pagination } from '../../components/pagination/pagination';
import { SaleFilterPanel } from '../../components/sale-filter-panel/sale-filter-panel';

@Component({
  imports: [CommonModule, Pagination, SaleFilterPanel],

  selector: 'app-sales-list',
  styleUrl: './sales-list.css',
  templateUrl: './sales-list.html',
})
export class SalesList implements OnInit {
  private saleService = inject(SaleService);

  sales = signal<SaleResponse[]>([]);
  isLoading = signal<boolean>(true);

  page = signal(0);
  size = signal(10);
  totalPages = signal(0);

  filters = signal<SaleFilters>({});

  showFilters = signal(false);
  expandedId = signal<string | null>(null);

  statusLabels: Record<string, string> = {
    PENDING: 'Pendiente',
    APPROVED: 'Aprobada',
    REJECTED: 'Rechazada',
  };

  toggleFilters() {
    this.showFilters.update(v => !v);
  }

  onApply(f: SaleFilters) {
    this.filters.set(f);
    this.page.set(0);
    this.loadSales();
    this.showFilters.set(false);
  }

  ngOnInit() {
    this.loadSales();
  }

  loadSales() {
    this.isLoading.set(true);
    this.expandedId.set(null);

    window.scrollTo({ top: 0, behavior: 'smooth' });

    this.saleService.getAll(this.page(), this.size(), this.filters()).subscribe({
      next: (data) => {
        this.sales.set(data.content);
        this.totalPages.set(data.totalPages);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar las ventas', err);
        this.isLoading.set(false);
      }
    });
  }

  goToPage(page: number) {
    this.page.set(page);
    this.loadSales();
  }

  toggleDetail(id: string) {
    this.expandedId.update(current => current === id ? null : id);
  }
}