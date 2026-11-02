import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { GameService } from '../../services/game-service';
import { Game } from '../../models/game';
import { Reviews } from '../../components/reviews/reviews';

@Component({
  imports: [RouterLink, Reviews],
  selector: 'app-game-detail',
  styleUrl: './game-detail.css',
  templateUrl: './game-detail.html',
})
export class GameDetail implements OnInit{

  private route = inject(ActivatedRoute);
  private gameService = inject(GameService);

  gameId = signal<string>(''); 
  game = signal<Game | null>(null);
  isLoading = signal(true);
  notFound = signal(false); 

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.notFound.set(true);
      this.isLoading.set(false);
      return;
    }

    this.gameId.set(id);

    this.gameService.getGameById(id).subscribe({
      next: (data) => {
        this.game.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar el juego', err);
        this.notFound.set(true);
        this.isLoading.set(false);
      },
    });
  }



}
