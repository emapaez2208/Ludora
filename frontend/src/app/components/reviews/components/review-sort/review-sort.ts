import { Component, ElementRef, inject, input, output, signal } from '@angular/core';
import { ReviewSortOptions } from '../../../../models/review-filters';

@Component({
  imports: [],
  selector: 'app-review-sort',
  styleUrl: './review-sort.css',
  templateUrl: './review-sort.html',
  host: { '(document:click)': 'onDocumentClick($event)' }
})
export class ReviewSort {
  private host = inject(ElementRef);

  sort = input<ReviewSortOptions>({ sortBy: 'date', sortDirection: 'desc' });
  sortChange = output<ReviewSortOptions>();

  open = signal(false);

  options: { label: string; value: ReviewSortOptions }[] = [
    { label: 'Más recientes', value: { sortBy: 'date', sortDirection: 'desc' } },
    { label: 'Más antiguas', value: { sortBy: 'date', sortDirection: 'asc' } },
    { label: 'Mejor calificadas', value: { sortBy: 'rating', sortDirection: 'desc' } },
    { label: 'Peor calificadas', value: { sortBy: 'rating', sortDirection: 'asc' } },
  ];

  toggle() {
    this.open.update(v => !v);
  }

  isActive(option: ReviewSortOptions) {
    return option.sortBy === this.sort().sortBy
        && option.sortDirection === this.sort().sortDirection;
  }

  select(option: ReviewSortOptions) {
    this.sortChange.emit(option);
    this.open.set(false);
  }

  onDocumentClick(event: Event) {
    if (this.open() && !this.host.nativeElement.contains(event.target)) {
      this.open.set(false);
    }
  }
}