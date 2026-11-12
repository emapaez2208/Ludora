import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Game } from '../models/game';
import { PageResponse } from '../models/page-response';

@Service()
export class SolicitudService {

  private http : HttpClient = inject(HttpClient);
  private readonly url = "/api/games";

 // Usar el endpoint de admin para filtrar por estado: 'all', 'blocked', 'needRevision'
  getAdminGames(status?: string, page: number = 0, size: number = 18) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (status) {
      params = params.set('status', status);
    }

    return this.http.get<PageResponse<Game>>(`${this.url}/admin`, { params });
  }
  
   // Autoriza / Desbloquea el juego (Endpoint exclusivo de Admin para poner statusBlocked y needRevision en false)
   
  authorizeGame(externalId: string) {
    return this.http.patch<Game>(`${this.url}/${externalId}/AuthorizedGame`, {});
  }

  desautorizedGame(externalId: string){
    return this.http.delete<void>(`${this.url}/${externalId}/DesAuthorizedGame`,{});

  }
}
