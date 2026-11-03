import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Game } from '../models/game';
import { PageResponse } from '../models/game';

@Service()
export class SolicitudService {

    private http : HttpClient = inject(HttpClient);
    private readonly url = "/api/games";

    // Probar si anda
    getGamesStatusBloked(statusBlocked?: boolean){
        let params = new HttpParams();

        if(statusBlocked !== undefined){
            params = params.set('statusBlocked',statusBlocked.toString());
            
        }
        return this.http.get<PageResponse<Game>>(this.url ,{params})
    }

    
   //Obtiene los juegos que necesitan revisión (/api/games/needRevision)
   
  getGamesNeedRevision(){
    return this.http.get<Game[]>(`${this.url}/needRevision`);
  }


  // Actualización general (para developers)
  
  putGames(game: Game){
    return this.http.put<Game>(`${this.url}/${game.externalId}`, game);
  }

  
   // Autoriza / Desbloquea el juego (Endpoint exclusivo de Admin para poner statusBlocked y needRevision en false)
   
  authorizeGame(externalId: string){
    return this.http.put<Game>(`${this.url}/${externalId}/authorized`, {});
  }
}
