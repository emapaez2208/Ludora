import { Component, inject, OnInit, signal } from '@angular/core';
import { GameService } from '../../services/game-service';
import { Game } from '../../models/game';
import { GameFilters } from '../../models/game-filters';
import { GameSortOptions } from '../../models/game-sort-options';
import { GameCard } from '../../components/game-card/game-card';
import { GameFilterPanel } from '../../components/game-filter-panel/game-filter-panel';
import { GameSort } from '../../components/game-sort/game-sort';
import { Pagination } from '../../components/pagination/pagination';

@Component({
  imports: [GameCard, GameFilterPanel, GameSort, Pagination],
  selector: 'app-game-management',
  styleUrl: './game-management.css',
  templateUrl: './game-management.html',
})
export class GameManagement implements OnInit {
  private gameService = inject(GameService);

  tabs = [
    { key: 'all', label: 'Todos los juegos' },
    { key: 'blocked', label: 'Bloqueados' },
    { key: 'needRevision', label: 'Necesitan revisión' },
  ];

  tab = signal('all');

  games = signal<Game[]>([]);
  isLoading = signal(true);
  errorMsg = signal('');

  page = signal(0);
  size = signal(18);
  totalPages = signal(0);

  filters = signal<GameFilters>({});
  sort = signal<GameSortOptions>({ sortBy: 'name', sortDirection: 'asc' });
  
  showFilters = signal(false);

  ngOnInit() {
    this.loadGames();
  }

  toggleFilters() {
    this.showFilters.update(v => !v);
  }

  selectTab(key: string) {

    if (key === this.tab()) return;
    this.tab.set(key);
    this.page.set(0);
    this.loadGames();
  
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

  goToPage(page: number) {
    this.page.set(page);
    this.loadGames();
  }

  loadGames() {

    this.isLoading.set(true);
    this.errorMsg.set('');
    window.scrollTo({ top: 0, behavior: 'smooth' });

    this.gameService
      .getAdminGames(this.page(), this.size(), this.filters(), this.sort(), this.tab())
      .subscribe({

        next: (data) => {
          this.games.set(data.content);
          this.totalPages.set(data.totalPages);
          this.isLoading.set(false);
        },

        error: (err) => {
          console.error('Error al cargar los juegos', err);
          this.errorMsg.set('No se pudieron cargar los juegos.');
          this.isLoading.set(false);
        },

      });
  }
}
