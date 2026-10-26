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

  status? = signal<Boolean | undefined>(undefined);

  ngOnInit(): void {
    this.loadSolicitudes();
  }

  loadSolicitudes(statusBlocked?: boolean): void {
    this.status?.set(statusBlocked);
    this.sSolicitud.getGamesStatusBloked(statusBlocked).subscribe({
      next: (data) => this.games.set(data.content), 
      error: (e) => console.log('Acá hay un error al cargar solicitudes:', e)
    });
  }


}

