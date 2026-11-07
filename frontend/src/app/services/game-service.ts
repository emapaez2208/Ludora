import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Game } from '../models/game';
import { Observable } from 'rxjs';
import { GameFilters } from '../models/game-filters';
import { GameRequest } from '../models/game-request';
import { PageResponse } from '../models/page-response';
import { GameSortOptions } from '../models/game-sort-options';

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
      filters: GameFilters = {},
      sort: GameSortOptions = { sortBy: 'name', sortDirection: 'asc' }
    ): Observable<PageResponse<Game>> {
      let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
        .set('sortBy', sort.sortBy)
        .set('sortDirection', sort.sortDirection);

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

    getAdminGames(
      page: number,
      size: number,
      filters: GameFilters,
      sort: GameSortOptions,
      status: string
    ): Observable<PageResponse<Game>> {

      let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
        .set('sortBy', sort.sortBy)
        .set('sortDirection', sort.sortDirection)
        .set('status', status);

      const { genreNames, ...simple } = filters;

      Object.entries(simple).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== '') {
          params = params.set(key, String(value));
        }
        
      });

      genreNames?.forEach(g => {
        params = params.append('genreNames', g);
      });

      return this.http.get<PageResponse<Game>>(`${this.url}/admin`, { params });
    }

    getGameById(externalId: string): Observable<Game> {
      return this.http.get<Game>(`${this.url}/${externalId}`);
    }

    getGame(id : string){
      return this.http.get<Game>(`${this.url}/${id}`)
    }


    createGame(game : GameRequest){
      return this.http.post<Game>(this.url, game)
    }

    editGame(id : string, game : GameRequest){
      return this.http.put<Game>(`${this.url}/${id}`, game)
    }


    getClientGames(){
      return this.http.get<Game[]>('/api/clients/library')
    }
    
    getDeveloperGames(id : string){
      return this.http.get<Game[]>(`/api/developers/${id}/games`)
    }
}
