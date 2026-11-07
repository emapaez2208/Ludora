import { Component, effect, inject, signal, untracked } from '@angular/core';
import { GameService } from '../../services/game-service';
import { Game } from '../../models/game';
import { UserStateService } from '../../services/user-state-service';
import { GameCard } from '../../components/game-card/game-card';
import { Pagination } from '../../components/pagination/pagination';
import { GameFilterPanel } from '../../components/game-filter-panel/game-filter-panel';
import { GameFilters } from '../../models/game-filters';

@Component({
  imports: [GameCard, Pagination, GameFilterPanel],
  selector: 'app-library',
  styleUrl: './library.css',
  templateUrl: './library.html',
})
export class Library {

  private gameService = inject(GameService)
  private userState = inject(UserStateService)
  
  allGames = signal<Game[]>([])
  games = signal<Game[]>([])
  isLoading = signal<boolean>(true)
  msjError = signal('')

  filters = signal<GameFilters>({})
  showFilters = signal(false)
  page = signal(0);
  size = signal(18);
  totalPages = signal(0);

  toggleFilters() {
    this.showFilters.update(v => !v)
  }

  isDeveloper(): boolean {
    if (this.userState.developer()) {
      return true;
    }
    return false;
  }

  constructor() {
    effect(() => {
        if (this.userState.loaded()) {
            untracked(() => this.loadGames())
        }
    })
  }
  

  loadGames() {
    this.isLoading.set(true);
    this.msjError.set('');

    if (this.userState.client()) {

      this.gameService.getClientGames().subscribe({
        next: data => {
          this.allGames.set(data);
          this.page.set(0);
          this.applyFilters();
          this.isLoading.set(false);
        },
        error: e => {
          console.error(e);
          this.msjError.set('No se pudieron cargar los juegos del usuario');
          this.isLoading.set(false);
        }
      });

    } else if (this.userState.developer()) {

      const developer = this.userState.developer()!;

      this.gameService.getDeveloperGames(developer.externalId).subscribe({
        next: data => {
          this.allGames.set(data);
          this.page.set(0);
          this.applyFilters();
          this.isLoading.set(false);
        },
        error: e => {
          console.error(e);
          this.msjError.set('No se pudieron cargar los juegos del desarrollador');
          this.isLoading.set(false);
        }
      });

    } else {
      this.msjError.set('No se pudo identificar al usuario');
      this.isLoading.set(false);
    }
  }

  onApply(filters: GameFilters) {
    this.filters.set(filters);
    this.page.set(0);
    this.applyFilters();
    this.showFilters.set(false);
  }

  applyFilters() {
    const f = this.filters();

    let filtered = [...this.allGames()];

    if (f.name?.trim()) {

      filtered = filtered.filter(game =>
        game.name.toLowerCase().includes(f.name!.trim().toLowerCase())
      );
    }

    if (f.rangeName) {
      filtered = filtered.filter(game =>
        game.ageRange.toLowerCase() === f.rangeName!.toLowerCase()
      );
    }

    if (f.genreNames?.length) {
      filtered = filtered.filter(game =>
        game.genres.some(genre =>
          f.genreNames!.includes(genre)
        )
      );
    }

    // Calcular cantidad de páginas
    this.totalPages.set(Math.ceil(filtered.length / this.size()));

    // Obtener los juegos de la página actual
    const start = this.page() * this.size();
    const end = start + this.size();

    this.games.set(filtered.slice(start, end));
  }

  goToPage(page: number) {
    this.page.set(page);
    this.applyFilters();
  }

}
