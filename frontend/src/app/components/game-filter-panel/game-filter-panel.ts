import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { AgeRangeService } from '../../services/age-range-service';
import { GenreService } from '../../services/genre-service';
import { DeveloperService } from '../../services/developer-service';
import { GameFilters } from '../../models/game-filters';
import { AgeRange } from '../../models/age-range';
import { Genre } from '../../models/genre';

@Component({
  imports: [],
  selector: 'app-game-filter-panel',
  styleUrl: './game-filter-panel.css',
  templateUrl: './game-filter-panel.html',
})
export class GameFilterPanel implements OnInit {
  
  private ageRangeService = inject(AgeRangeService);
  private genreService = inject(GenreService);
  private developerService = inject(DeveloperService);
  
  initial = input<GameFilters>({});
  apply = output<GameFilters>();

  filters = signal<GameFilters>({});
  ageRanges = signal<AgeRange[]>([]);
  genres = signal<Genre[]>([]);
  companies = signal<string[]>([]);

  today = new Date().toLocaleDateString('en-CA');

  ngOnInit() {
    this.filters.set({ ...this.initial() });

    this.ageRangeService.getAgeRanges().subscribe({
      next: (d) => this.ageRanges.set([...d].sort((a, b) => a.minAge - b.minAge)),
      error: (e) => console.error('Error al cargar clasificaciones', e),
    });

    this.genreService.getGenres().subscribe({
      next: (d) => this.genres.set(d),
      error: (e) => console.error('Error al cargar géneros', e),
    });

    this.developerService.getCompanies().subscribe({
      next: (d) => this.companies.set(d),
      error: (e) => console.error('Error al cargar desarrolladoras', e),
    });
  }

  update(field: keyof GameFilters, value: unknown) {
    this.filters.update(f => ({ ...f, [field]: value || null }));
  }

  hasGenre(name: string) {
    return this.filters().genreNames?.includes(name) ?? false;
  }

  toggleGenre(name: string) {
    this.filters.update(f => {
      const current = f.genreNames ?? [];
      const next = current.includes(name)
        ? current.filter(g => g !== name)
        : [...current, name];
      return { ...f, genreNames: next };
    });
  }

  get priceInvalid() {
    const { minPrice, maxPrice } = this.filters();
    return minPrice != null && maxPrice != null && Number(minPrice) > Number(maxPrice);
  }

  get dateInvalid() {
    const { minReleaseDate, maxReleaseDate } = this.filters();
    return !!minReleaseDate && !!maxReleaseDate && minReleaseDate > maxReleaseDate;
  }

  submit() {
    if (this.priceInvalid || this.dateInvalid) return;
    this.apply.emit(this.filters());
  }

  reset() {
    this.filters.set({});
    this.apply.emit({});
  }
}


