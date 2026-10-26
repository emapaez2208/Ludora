import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Game } from '../models/game';
import { PageResponse } from '../models/game';

@Service()
export class SolicitudService {

    private http : HttpClient = inject(HttpClient);
    private readonly url = "/api/games";

    getGamesStatusBloked(statusBlocked?: boolean){
        let params = new HttpParams();

        if(statusBlocked !== undefined){
            params = params.set('statusBlocked',statusBlocked.toString());
            
        }
        return this.http.get<PageResponse<Game>>(this.url ,{params})
    }

    putGames( game : Game){ 
        return this.http.put<Game>(`${this.url}/${game.externalId}`,game)
    }
}
