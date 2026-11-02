import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search',
  standalone: true,
  templateUrl: './search.html',
  styleUrl: './search.css',
})
export class Search {
  private router = inject(Router);

  onSearch(value: string): void {
    const query = value.trim();

    if (query) {
      this.router.navigate(['/catalog'], {
        queryParams: { name: query }
      });
    } else {
      this.router.navigate(['/catalog']);
    }
  }
}