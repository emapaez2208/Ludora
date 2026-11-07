import { Component, inject, OnInit, signal } from '@angular/core';
import { Game } from '../../models/game'; 
import { SolicitudService } from '../../services/solicitud-service';

@Component({
  imports: [],
  selector: 'app-solicitud-list',
  styleUrl: './solicitud-list.css',
  templateUrl: './solicitud-list.html',
})
export class SolicitudList implements OnInit {

 games = signal<Game[]>([]);
  private sSolicitud = inject(SolicitudService);

  status = signal<boolean | undefined>(undefined);
  
  // Variable opcional para saber si estás viendo la vista de revisión
  isRevisionView = signal<boolean>(false);

  ngOnInit(): void {
    this.loadSolicitudes();
  }

  // Cargar por estado de bloqueo
  loadSolicitudes(statusBlocked?: boolean): void {
    this.isRevisionView.set(false);
    this.status.set(statusBlocked);

    this.sSolicitud.getGamesStatusBloked(statusBlocked).subscribe({
      next: (data) => this.games.set(data.content), 
      error: (e) => console.log('Error al cargar solicitudes:', e)
    });
  }

  // Cargar específicamente los que necesitan revisión (/api/games/needRevision)
  loadGamesNeedRevision(): void {
    this.isRevisionView.set(true);
    this.status.set(undefined);

    this.sSolicitud.getGamesNeedRevision().subscribe({
      next: (data) => this.games.set(data), 
      error: (e) => console.log('Error al cargar juegos en revisión:', e)
    });
  }

  // Autorizar y refrescar dependiendo de qué vista estés usando
  authorizeGame(externalId: string): void {
    this.sSolicitud.authorizeGame(externalId).subscribe({
      next: () => {
        console.log('Juego autorizado exitosamente');
        
        // Si estabas en la vista de revisión, recargas esa misma; si no, mantienes el filtro de estado
        if (this.isRevisionView()) {
          this.loadGamesNeedRevision();
        } else {
          this.loadSolicitudes(this.status());
        }
      },
      error: (e) => console.log('Error al autorizar el juego:', e)
    });
  }
  


}

