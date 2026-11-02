import { Component, computed, input, output } from '@angular/core';

@Component({
  imports: [],
  selector: 'app-pagination',
  styleUrl: './pagination.css',
  templateUrl: './pagination.html',
})
export class Pagination {

  page = input.required<number>();
  totalPages = input.required<number>();
  pageChange = output<number>();

    visiblePages = computed(() => {
      const total = this.totalPages();
      const current = this.page() + 1;
      if (total <= 5) {
        return Array.from({ length: total }, (_, i) => i + 1);
      }
      if (current <= 3) {
        return [1, 2, 3, 4, '...', total];
      } else if (current >= total - 2) {
        return [1, '...', total - 3, total - 2, total - 1, total];
      } else {
        return [1, '...', current - 1, current, current + 1, '...', total];
      }
    });

    goTo(p: number | string) {
    if (typeof p === 'number') {
      this.pageChange.emit(p - 1);
    }
  }

  prev() {
    if (this.page() > 0) {
      this.pageChange.emit(this.page() - 1);
    }
  }

  next() {
    if (this.page() < this.totalPages() - 1) {
      this.pageChange.emit(this.page() + 1);
    }
  }

}
