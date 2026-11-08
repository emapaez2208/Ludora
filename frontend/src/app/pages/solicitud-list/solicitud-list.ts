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
  status = signal<boolean | undefined>(undefined);
  // Variable opcional para saber si estás viendo la vista de revisión
  isRevisionView = signal<boolean>(false);

  //Variables para la Paginacion
  page = signal(0)
  size = signal(18);
  totalPages = signal(0)
  loading = signal(false)
  error = signal('')

  ngOnInit(): void {
    this.loadSolicitudes();
  }


  // Cargar por estado de bloqueo
  loadSolicitudes(statusBlocked?: boolean , resetPage : boolean = true): void {
    if(resetPage){
      this.page.set(0);
    }
    
    this.isRevisionView.set(false);
    this.status.set(statusBlocked);
    this.loading.set(true);
    this.error.set('');

    this.sSolicitud.getGamesStatusBloked(statusBlocked , this.page() , this.size()).subscribe({
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

  // Cargar específicamente los que necesitan revisión (/api/games/needRevision)
  loadGamesNeedRevision(resetPage: boolean = true): void {
    if(resetPage){
      this.page.set(0);
    }
    this.isRevisionView.set(true);
    this.status.set(undefined);
    this.loading.set(true);
    this.error.set('');

    this.sSolicitud.getGamesNeedRevision(this.page() , this.size()).subscribe({
      next: (data) => {this.games.set(data.content);
                       this.totalPages.set(data.totalPages);
                       this.loading.set(false);}, 
      error: (e) => {console.log('Error al cargar juegos en revisión:', e);
                     this.loading.set(false);
                     this.error.set('No se pudieron cargar los datos');}
    });
  }

  // Método que ejecuta el componente hijo <app-pagination> al hacer clic en un número
  goToPage(page: number): void {
    this.page.set(page);

    // Recargamos según la vista actual pero pasandole `false` para que NO resetee la página a 0
    if (this.isRevisionView()) {
      this.loadGamesNeedRevision(false);
    } else {
      this.loadSolicitudes(this.status(), false);
    }
  }

  // Autorizar y refrescar dependiendo de qué vista estés usando
  authorizeGame(externalId: string): void {
    this.sSolicitud.authorizeGame(externalId).subscribe({
      next: () => {
        console.log('Juego autorizado exitosamente');
        
        // Si estabas en la vista de revisión, recargas esa misma; si no, mantienes el filtro de estado
        if (this.isRevisionView()) {
          this.loadGamesNeedRevision(false);
        } else {
          this.loadSolicitudes(this.status(), false);
        }
      },
      error: (e) => console.log('Error al autorizar el juego:', e)
    });
  }

  // Desautorizar y refrescar manteniendo la página actual (false)
  desautorizedGame(externalId: string): void {
    this.sSolicitud.desautorizedGame(externalId).subscribe({
      next: () => {
        console.log('Juego desautorizado exitosamente');
        
        if (this.isRevisionView()) {
          this.loadGamesNeedRevision(false);
        } else {
          this.loadSolicitudes(this.status(), false);
        }
      },
      error: (e) => console.log('Error al desautorizar el juego:', e)
    });
  }


  


}

