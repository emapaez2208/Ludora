import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Game } from '../../models/game';
import { Carousel } from './components/carousel/carousel';
import { GameService } from '../../services/game-service';
import { CategorySlide } from './components/category-slide/category-slide';
import { Genre } from '../../models/genre';
import { GenreService } from '../../services/genre-service'; 
import { Search } from './components/search/search';
import { CompanySlide } from './components/company-slide/company-slide';
import { DeveloperService } from '../../services/developer-service';
import { GameTabs } from './components/game-tabs/game-tabs';
import { RouterLink } from '@angular/router';

export interface FilterChip {
  id: string;
  label: string;
  queryParam: string;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, Search, Carousel, CategorySlide, GameTabs, CompanySlide, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private gameService = inject(GameService);
  private genreService = inject(GenreService);
  private devService = inject(DeveloperService)

  featuredGames = signal<Game[]>([]);
  tabGames = signal<Game[]>([]);
  genres = signal<Genre[]>([]);
  isLoading = signal<boolean>(true);
  companies = signal<string[]>([]);

  ngOnInit(): void {
    this.loadFeaturedGames();
    this.loadTabGames();
    this.loadGenres(); 
    this.loadCompanies();
  }

  loadFeaturedGames(): void {
    this.isLoading.set(true);
    this.gameService.getGames(0, 8).subscribe({
      next: (data) => {
        this.featuredGames.set(data.content);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar juegos destacados para el home', err);
        this.isLoading.set(false);
      }
    });
  }

  loadTabGames(): void {
    // Pedimos 30 o 40 juegos para tener variedad de precios y géneros
    this.gameService.getGames(0, 30).subscribe({
      next: (data) => {
        this.tabGames.set(data.content);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar juegos para tabs', err);
        this.isLoading.set(false);
      }
    });
  }

  loadGenres(): void {
    this.genreService.getGenres().subscribe({
      next: (data) => {
        this.genres.set(data);
      },
      error: (err) => {
        console.error('Error al cargar generos en el home', err);
      }
    });
  }
  loadCompanies():void{
    this.devService.getCompanies().subscribe({
      next: (data) => this.companies.set(data),
      error: (err) => console.error('Error al cargar empresas:', err)
    });
  }

}