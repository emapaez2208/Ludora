import { Component, input, OnInit, output, signal } from '@angular/core';
import { SaleFilters } from '../../models/sale-filters';

@Component({
  imports: [],
  selector: 'app-sale-filter-panel',
  styleUrl: './sale-filter-panel.css',
  templateUrl: './sale-filter-panel.html',
})
export class SaleFilterPanel implements OnInit {

  initial = input<SaleFilters>({});
  apply = output<SaleFilters>();

  filters = signal<SaleFilters>({});

  today = new Date().toLocaleDateString('en-CA');

  ngOnInit() {
    this.filters.set({ ...this.initial() });
  }

  update(field: keyof SaleFilters, value: unknown) {
    this.filters.update(f => ({ ...f, [field]: value || null }));
  }

  get priceInvalid() {
    const { minPrice, maxPrice } = this.filters();
    return minPrice != null && maxPrice != null && Number(minPrice) > Number(maxPrice);
  }

  get dateInvalid() {
    const { minDate, maxDate } = this.filters();
    return !!minDate && !!maxDate && minDate > maxDate;
  }

  submit() {
    if (this.priceInvalid || this.dateInvalid) return;
    this.apply.emit(this.filters());
  }

  reset() {
    this.filters.set({});
    this.apply.emit({});
  }
}