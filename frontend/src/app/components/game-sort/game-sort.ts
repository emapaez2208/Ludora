import { Component, ElementRef, HostListener, inject, input, output, signal } from '@angular/core';
import { GameSortOptions } from '../../models/game-sort-options';

@Component({
  imports: [],
  selector: 'app-game-sort',
  styleUrl: './game-sort.css',
  templateUrl: './game-sort.html',
  host: { '(document:click)': 'onDocumentClick($event)'}
})
export class GameSort {
  private host = inject(ElementRef);

  sort = input<GameSortOptions>({ sortBy: 'name', sortDirection: 'asc' });
  sortChange = output<GameSortOptions>();

  open = signal(false);

  options: { label: string; value: GameSortOptions }[] = [
    { label: 'Nombre (A-Z)', value: { sortBy: 'name', sortDirection: 'asc' } },
    { label: 'Nombre (Z-A)', value: { sortBy: 'name', sortDirection: 'desc' } },
    { label: 'Precio (menor a mayor)', value: { sortBy: 'price', sortDirection: 'asc' } },
    { label: 'Precio (mayor a menor)', value: { sortBy: 'price', sortDirection: 'desc' } },
    { label: 'Más recientes', value: { sortBy: 'releaseDate', sortDirection: 'desc' } },
    { label: 'Más antiguos', value: { sortBy: 'releaseDate', sortDirection: 'asc' } },
  ];

  toggle() {
    this.open.update(v => !v);
  }

  isActive(option: GameSortOptions) {
    return option.sortBy === this.sort().sortBy
        && option.sortDirection === this.sort().sortDirection;
  }

  select(option: GameSortOptions) {
    this.sortChange.emit(option);
    this.open.set(false);
  }

  onDocumentClick(event: Event) {
    if (this.open() && !this.host.nativeElement.contains(event.target)) {
      this.open.set(false);
    }
  }
  
}
