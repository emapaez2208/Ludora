import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Game } from '../models/game';
import { PageResponse } from '../models/page-response';

@Service()
export class SolicitudService {

  private http : HttpClient = inject(HttpClient);
  private readonly url = "/api/games";

    // Probar si anda
  getGamesStatusBloked(statusBlocked?: boolean , page: number = 0, size: number = 18){
        let params = new HttpParams();

        if(statusBlocked !== undefined){
            params = params.set('statusBlocked',statusBlocked.toString());
            
        }

        params = params.set('page', page.toString());
        params = params.set('size', size.toString());
        return this.http.get<PageResponse<Game>>(this.url ,{ params })
    }

    
   //Obtiene los juegos que necesitan revisión (/api/games/needRevision)
   
  getGamesNeedRevision(page: number=0 , size: number=18){

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<Game>>(`${this.url}/needRevision`,{params});
  }


  // Actualización general (para developers)
  
  putGames(game: Game){
    return this.http.put<Game>(`${this.url}/${game.externalId}`, game);
  }

  
   // Autoriza / Desbloquea el juego (Endpoint exclusivo de Admin para poner statusBlocked y needRevision en false)
   
  authorizeGame(externalId: string) {
    return this.http.patch<Game>(`${this.url}/${externalId}/AuthorizedGame`, {});
  }

  desautorizedGame(externalId: string){
    return this.http.delete<void>(`${this.url}/${externalId}/DesAuthorizedGame`,{});

  }
}
