import { Component, effect, inject, OnInit, signal, untracked } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { GameService } from '../../services/game-service';
import { Game } from '../../models/game';
import { Reviews } from '../../components/reviews/reviews';
import { CartService } from '../../services/cart-service';
import { ToastService } from '../../services/toast-service';
import { AuthService } from '../../services/auth-service';
import { UserStateService } from '../../services/user-state-service';

@Component({
  imports: [RouterLink, Reviews],
  selector: 'app-game-detail',
  styleUrl: './game-detail.css',
  templateUrl: './game-detail.html',
})
export class GameDetail implements OnInit{

  private route = inject(ActivatedRoute);
  private gameService = inject(GameService);
  private toastService = inject(ToastService);
  private userState = inject(UserStateService);

  cartService = inject(CartService);
  authService = inject(AuthService);

  gameId = signal<string>(''); 
  game = signal<Game | null>(null);
  isLoading = signal(true);
  notFound = signal(false);
  isActing = signal(false);
  isOwner = signal(false);

  constructor() {
    effect(() => {
      const developer = this.userState.developer();
      const game = this.game();

      if (developer && game) { 
        untracked(() => this.checkOwner(developer.externalId, game.externalId)); // chequea si el developer es el dueño del juego
      }
    });
  }

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

  checkOwner(developerId: string, gameId: string) {

    this.gameService.getDeveloperGames(developerId).subscribe({
      
      next: (games) => this.isOwner.set(games.some(g => g.externalId === gameId)),
      
      error: () => this.isOwner.set(false)
    });
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  isClient(): boolean {
    return this.authService.isClient();
  }

  approve() {
    this.isActing.set(true);

    this.gameService.authorizeGame(this.gameId()).subscribe({
      next: (data) => {
        this.game.set(data);
        this.isActing.set(false);
        this.toastService.show('El juego quedó disponible para la venta.', 'info');
      },

      error: () => {
        this.isActing.set(false);
        this.toastService.show('No se pudo completar la acción.', 'error');
      }
    });
  }

  block(message: string) {
    this.isActing.set(true);

    this.gameService.blockGame(this.gameId()).subscribe({
      next: () => {
        this.gameService.getGameById(this.gameId()).subscribe({
          next: (data) => {
            this.game.set(data);
            this.isActing.set(false);
            this.toastService.show(message, 'info');
          },
          error: () => this.isActing.set(false)
        });
      },

      error: () => {
        this.isActing.set(false);
        this.toastService.show('No se pudo completar la acción.', 'error');
      }
    });
  }


}
