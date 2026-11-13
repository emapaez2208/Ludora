import { Component, computed, input, signal, effect, inject, untracked } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Game } from '../../../../models/game';
import { GameService } from '../../../../services/game-service';
import { ReviewService } from '../../../../services/review-service';
import { ReviewSummary } from '../../../../models/review';
import { map, Observable } from 'rxjs';

export type TabType = 'popular' | 'top-rated' | 'free';

export interface TabOption {
  id: TabType;
  label: string;
  emptyMessage: string;
}

interface RatingInfo {
  label: string;
  css: 'positive' | 'mixed' | 'negative' | 'neutral';
  detail: string;
}

@Component({
  selector: 'app-game-tabs',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './game-tabs.html',
  styleUrl: './game-tabs.css',
})
export class GameTabs {
  private gameService = inject(GameService);
  private reviewService = inject(ReviewService);

  tabs: TabOption[] = [
    { id: 'popular', label: 'Populares', emptyMessage: 'Todavía no hay juegos populares.' },
    { id: 'top-rated', label: 'Mejor puntuados', emptyMessage: 'Todavía no hay suficientes reseñas para armar el ranking.' },
    { id: 'free', label: 'Gratuitos', emptyMessage: 'No hay juegos gratuitos por ahora.' }
  ];

  activeTab = signal<TabType>('popular');
  hoveredGame = signal<Game | null>(null);

  //cada pestaña consulta la base al abrirla
  private gamesByTab = signal<Partial<Record<TabType, Game[]>>>({});
  private inFlight = new Set<TabType>();
  private loadingTab = signal<TabType | null>(null);

  private summaries = signal<Record<string, ReviewSummary>>({});
  private pendingSummaries = new Set<string>();

  currentGames = computed<Game[]>(() => this.gamesByTab()[this.activeTab()] ?? []);
  isLoading = computed(() => this.loadingTab() === this.activeTab());
  currentEmptyMessage = computed(
    () => this.tabs.find(t => t.id === this.activeTab())?.emptyMessage ?? ''
  );

  hoveredRating = computed<RatingInfo>(() => {
    const game = this.hoveredGame();
    const summary = game ? this.summaries()[game.externalId] : undefined;
    return this.describeRating(summary);
  });

  constructor() {
    this.loadTab('popular');

    // Al cambiar el juego resaltado se pide su valoración (si todavía no la tenemos)
    effect(() => {
      const game = this.hoveredGame();
      if (game) {
        untracked(() => this.loadSummary(game.externalId));
      }
    });
  }

    selectTab(tab: TabType): void {
    this.activeTab.set(tab);
    this.loadTab(tab);
  }

    private loadTab(tab: TabType): void {
    const cached = this.gamesByTab()[tab];
    if (cached) {
      this.hoveredGame.set(cached[0] ?? null);
      return;
    }

    this.loadingTab.set(tab);
    this.hoveredGame.set(null);

    if (this.inFlight.has(tab)) return;   // ya hay un pedido en camino para esta pestaña
    this.inFlight.add(tab);

    this.fetchTab(tab).subscribe({
      next: (games) => {
        this.inFlight.delete(tab);
        this.gamesByTab.update(c => ({ ...c, [tab]: games }));
        if (this.loadingTab() === tab) this.loadingTab.set(null);
        if (this.activeTab() === tab) this.hoveredGame.set(games[0] ?? null);
      },
      error: (err) => {
        // No se guarda en caché: si falla, se reintenta la próxima vez que se abra la pestaña
        console.error(`Error al cargar la pestaña ${tab}`, err);
        this.inFlight.delete(tab);
        if (this.loadingTab() === tab) this.loadingTab.set(null);
      }
    });
  }

  private fetchTab(tab: TabType): Observable<Game[]> {
    switch (tab) {
      case 'popular':
        return this.gameService.getPopular(10);
      case 'top-rated':
        return this.gameService.getTopRated(10, 3);
      case 'free':
        return this.gameService
          .getGames(0, 10, { maxPrice: 0 }, { sortBy: 'releaseDate', sortDirection: 'desc' })
          .pipe(map(page => page.content));
    }
  }

  private loadSummary(gameId: string): void {
    if (this.summaries()[gameId] || this.pendingSummaries.has(gameId)) return;

    this.pendingSummaries.add(gameId);
    this.reviewService.getSummary(gameId).subscribe({
      next: (summary) => {
        this.summaries.update(m => ({ ...m, [gameId]: summary }));
        this.pendingSummaries.delete(gameId);
      },
      error: () => this.pendingSummaries.delete(gameId)
    });
  }

  private describeRating(summary?: ReviewSummary): RatingInfo {
    if (!summary) {
      return { label: 'Cargando...', css: 'neutral', detail: '' };
    }
    if (summary.total === 0) {
      return { label: 'Sin reseñas', css: 'neutral', detail: '' };
    }

    const average = summary.average;
    const detail = `${average.toFixed(1)} ★ · ${summary.total} ${summary.total === 1 ? 'reseña' : 'reseñas'}`;

    if (average >= 4.5) return { label: 'Muy positivas', css: 'positive', detail };
    if (average >= 3.5) return { label: 'Positivas', css: 'positive', detail };
    if (average >= 2.5) return { label: 'Variadas', css: 'mixed', detail };
    return { label: 'Negativas', css: 'negative', detail };
  }
}