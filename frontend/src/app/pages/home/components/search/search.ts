import { Component, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../services/auth-service';

@Component({
  selector: 'app-search',
  standalone: true,
  templateUrl: './search.html',
  styleUrl: './search.css',
})
export class Search {
  private router = inject(Router);
  private authService = inject(AuthService);

  saludo = computed(() => {
    if (this.authService.isLoggedIn$()) {
      const username = this.authService.getUsername();
      if (username) {
        return `¡Hola, ${username}!`;
      }
    }
    return '¡Qué bueno verte en Ludora!';
  });

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