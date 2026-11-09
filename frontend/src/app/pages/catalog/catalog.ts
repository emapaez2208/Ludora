import { Component, inject, OnInit, signal } from '@angular/core';
import { GameService } from '../../services/game-service';
import { Game } from '../../models/game';
import { CommonModule } from '@angular/common';
import { GameFilters } from '../../models/game-filters';
import { RouterLink } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Pagination } from '../../components/pagination/pagination';
import { CartService } from '../../services/cart-service';
import { GameCard } from '../../components/game-card/game-card';
import { GameFilterPanel } from '../../components/game-filter-panel/game-filter-panel';
import { GameSort } from '../../components/game-sort/game-sort';
import { GameSortOptions } from '../../models/game-sort-options';
import { AuthService } from '../../services/auth-service';

@Component({
  imports: [CommonModule, RouterLink, Pagination, GameCard, GameFilterPanel, GameSort],

  selector: 'app-catalog',
  styleUrl: './catalog.css',
  templateUrl: './catalog.html',
})
export class Catalog implements OnInit {
  private gameService = inject(GameService);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  
  cartService = inject(CartService)

  games = signal<Game[]>([]);
  isLoading = signal<boolean>(true);

  page = signal(0);
  size = signal(18);
  totalPages = signal(0);

  filters = signal<GameFilters>({});

  sort = signal<GameSortOptions>({ sortBy: 'name', sortDirection: 'asc'});

  showFilters = signal(false);

  isClient(): boolean{
    return this.authService.isClient();
  }

  toggleFilters() {
    this.showFilters.update(v => !v);
  }

  onApply(f: GameFilters) {
    this.filters.set(f);
    this.page.set(0);
    this.loadGames();
    this.showFilters.set(false);
  }

  onSortChange(s: GameSortOptions) {
    this.sort.set(s);
    this.page.set(0);
    this.loadGames();
  }

  ngOnInit() {
    
    this.route.queryParams.subscribe(params => {
    const searchName = params['name']?.trim() || null;
    const searchCategory = params['category']?.trim() || null;
    const searchCompany = (params['company'] || params['developerCompany'])?.trim() || null;
    
    this.filters.set({
      ...(searchName ? { name: searchName } : {}),
      ...(searchCategory ? { genreNames: [searchCategory] } : {}),
      ...(searchCompany ? { developerCompany: searchCompany } : {})
    });

    this.page.set(0);
    this.loadGames();
  });
}

  loadGames() {
    this.isLoading.set(true);

    window.scrollTo({ top: 0, behavior: 'smooth' });

    this.gameService.getGames(this.page(), this.size(), this.filters(), this.sort()).subscribe({
      next: (data) => {
        this.games.set(data.content);
        this.totalPages.set(data.totalPages);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar el catálogo', err);
        this.isLoading.set(false);
      }
    });
  }

  goToPage(page: number) {
    this.page.set(page);
    this.loadGames();
  }

}