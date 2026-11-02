import { Component, computed, input, signal, effect } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Game } from '../../../../models/game';

export type TabType = 'general' | 'top-rated' | 'offers' | 'free';

export interface TabOption {
  id: TabType;
  label: string;
}

@Component({
  selector: 'app-game-tabs',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './game-tabs.html',
  styleUrl: './game-tabs.css',
})
export class GameTabs {
  games = input<Game[]>([]);

  tabs: TabOption[] = [
    { id: 'general', label: 'General' },
    { id: 'top-rated', label: 'Mejor puntuados' },
    { id: 'offers', label: 'Ofertas' },
    { id: 'free', label: 'Gratuitos' }
  ];

  activeTab = signal<TabType>('general');
  hoveredGame = signal<Game | null>(null);

  private shuffledGeneral = signal<Game[]>([]);

  constructor() {
    effect(() => {
      const source = this.games();
      if (source.length > 0 && this.shuffledGeneral().length === 0) {
        const shuffled = [...source].sort(() => Math.random() - 0.5);
        this.shuffledGeneral.set(shuffled);
        this.hoveredGame.set(shuffled[0] ?? null);
      }
    });
  }

  currentGames = computed<Game[]>(() => {
    const list = [...(this.games() ?? [])];
    const tab = this.activeTab();

    let filtered: Game[] = [];

    switch (tab) {
      case 'general':
        filtered = this.shuffledGeneral();
        break;

      case 'offers':
        filtered = list
          .filter(g => g.price > 0)
          .map(g => ({
            ...g,
            discountPercentage: 50,
            finalPrice: Number((g.price * 0.5).toFixed(2))
          }));
        break;

      case 'free':
        filtered = list.filter(g => g.price === 0);
        break;

      case 'top-rated':
        filtered = list;
        break;

      default:
        filtered = list;
        break;
    }

    return filtered.slice(0, 10);
  });

  selectTab(tabId: TabType): void {
    this.activeTab.set(tabId);
    const available = this.currentGames();
    this.hoveredGame.set(available.length > 0 ? available[0] : null);
  }
}