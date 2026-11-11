import { Component, inject, OnInit, signal } from '@angular/core';
import { Game } from '../../models/game'; 
import { SolicitudService } from '../../services/solicitud-service';
import { RouterLink } from '@angular/router';
import { Pagination } from '../../components/pagination/pagination';
import { DatePipe } from '@angular/common';


@Component({
  imports: [RouterLink , DatePipe ,Pagination],
  selector: 'app-solicitud-list',
  styleUrl: './solicitud-list.css',
  templateUrl: './solicitud-list.html',
})
export class SolicitudList implements OnInit {

  private sSolicitud = inject(SolicitudService);

  games = signal<Game[]>([]);
  currentStatus = signal<string>('all'); // 'all', 'blocked', 'needRevision'

  //Variables para la Paginacion
  page = signal(0)
  size = signal(18);
  totalPages = signal(0)
  loading = signal(false)
  error = signal('')

  ngOnInit(): void {
    this.loadSolicitudes('all');
  }


  // Cargar por estado de bloqueo
  loadSolicitudes(status: string , resetPage : boolean = true): void {
    if(resetPage){
      this.page.set(0);
    }
    
    this.currentStatus.set(status);
    this.loading.set(true);
    this.error.set('');

    this.sSolicitud.getAdminGames(status , this.page() , this.size()).subscribe({
      next: (data) =>{this.games.set(data.content);
                      this.totalPages.set(data.totalPages);
                      this.loading.set(false);
        },
      error: (e) => {console.log('Error al cargar solicitudes:', e);
                     this.loading.set(false);
                     this.error.set('No se pudieron cargar...')
      }
    });
  }

 goToPage(page: number): void {
    this.page.set(page);
    this.loadSolicitudes(this.currentStatus(), false);
  }

  // Autorizar y refrescar dependiendo de qué vista estés usando
  authorizeGame(externalId: string): void {
    this.sSolicitud.authorizeGame(externalId).subscribe({
      next: () => {
        console.log('Juego autorizado exitosamente');
        this.loadSolicitudes(this.currentStatus(), false);
      },
      error: (e) => console.log('Error al autorizar el juego:', e)
    });
  }

  // Desautorizar y refrescar manteniendo la página actual (false)
  desautorizedGame(externalId: string): void {
    this.sSolicitud.desautorizedGame(externalId).subscribe({
      next: () => {
        console.log('Juego desautorizado exitosamente');
        this.loadSolicitudes(this.currentStatus(), false);
      },
      error: (e) => console.log('Error al desautorizar el juego:', e)
    });
  }

}


