import { Component, input, OnInit, output, signal } from '@angular/core';
import { ReviewFilters } from '../../../../models/review-filters';

@Component({
  imports: [],
  selector: 'app-review-filter-panel',
  styleUrl: './review-filter-panel.css',
  templateUrl: './review-filter-panel.html',
})
export class ReviewFilterPanel implements OnInit {

  initial = input<ReviewFilters>({});
  canSearchUser = input(false);
  apply = output<ReviewFilters>();

  filters = signal<ReviewFilters>({});

  ratings = [1, 2, 3, 4, 5];
  today = new Date().toLocaleDateString('en-CA');

  ngOnInit() {
    this.filters.set({ ...this.initial() });
  }

  update(field: keyof ReviewFilters, value: unknown) {
    this.filters.update(f => ({ ...f, [field]: value || null }));
  }

  updateRating(field: 'minRating' | 'maxRating', value: string) {
    this.filters.update(f => ({ ...f, [field]: value ? Number(value) : null }));
  }

  get ratingInvalid() {
    const { minRating, maxRating } = this.filters();
    return minRating != null && maxRating != null && minRating > maxRating;
  }

  get dateInvalid() {
    const { minDate, maxDate } = this.filters();
    return !!minDate && !!maxDate && minDate > maxDate;
  }

  submit() {
    if (this.ratingInvalid || this.dateInvalid) return;
    this.apply.emit(this.filters());
  }

  reset() {
    this.filters.set({});
    this.apply.emit({});
  }
}