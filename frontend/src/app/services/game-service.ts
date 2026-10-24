import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Game, PageResponse } from '../models/game';
import { Observable } from 'rxjs';
import { GameFilters } from '../models/game-filters';

@Injectable({
  providedIn: 'root'
})
export class GameService {

    private http : HttpClient = inject(HttpClient);
    private url = "/api/games";

    constructor(){}

    getGames(
      page: number = 0, 
      size: number = 50,
      filters: GameFilters = {}  
    ): Observable<PageResponse<Game>> {
      let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());

      const { genreNames, ...simple} = filters;

      Object.entries(simple).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== '') {
          params = params.set(key, String(value));
        }
      });

      genreNames?.forEach(g => {
        params = params.append('genreNames', g);
      });

      return this.http.get<PageResponse<Game>>(this.url, { params });
    } 
}
