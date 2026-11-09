import { Component, computed, effect, inject, input, signal, untracked } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { ReviewService } from '../../services/review-service';
import { Review, ReviewRequest } from '../../models/review';
import { AuthService } from '../../services/auth-service';
import { ReviewDetailModalComponent } from './components/review-detail-modal/review-detail-modal';
import { GameService } from '../../services/game-service';
import { ToastService } from '../../services/toast-service';

@Component({
  selector: 'app-reviews',
  standalone: true,
  imports: [FormsModule, DatePipe, ReviewDetailModalComponent],
  templateUrl: './reviews.html',
  styleUrl: './reviews.css',
})
export class Reviews {
  gameId = input.required<string>();

  private reviewService = inject(ReviewService);
  private authService = inject(AuthService);
  private toastService = inject(ToastService);
  private gameService = inject(GameService);

  reviews = signal<Review[]>([]);
  currentPage = signal<number>(0);
  totalPages = signal<number>(1);
  totalElements = signal<number>(0);
  isLoading = signal<boolean>(false);

  ratingFilter = signal<number | null>(null);
  onlyMineFilter = signal<boolean>(false);

  showForm = signal<boolean>(false);
  newRating: number = 5;
  newComment: string = '';

  ownsGame = signal<boolean>(false) //booleano para determinar si el viewer de la reseña tiene el juego adquirido

  errorMessage = signal<string | null>(null);

  selectedReview = signal<Review | null>(null);

  // id del usuario logueado desde el token
  currentUserId = computed(() => this.authService.getClientExternalId());
  isClient = computed(() => this.authService.isClient());
  isDeveloper = computed(() => this.authService.isDeveloper());
  isAdmin = computed(() => this.authService.isAdmin());

  canWriteReview = computed(() => {
  return this.isClient() && this.ownsGame() && !this.userHasReviewed();
  });

  currentUsername = computed(() => this.authService.getUsername());
  isLoggedIn = computed(() => this.authService.isLoggedIn());

  // valida si el usuario actual ya tiene una reseña publicada
  userHasReviewed = computed(() => {
    const myId = this.currentUserId();
    if (!myId) return false;
    return this.reviews().some(r => r.clientExternalId === myId);
  });

  checkOwnerShips(): void{
    if(!this.isClient()){
      this.ownsGame.set(false);
      return;
    }

    this.gameService.getClientGames().subscribe({
      next:(games) => this.ownsGame.set(games.some(g => g.externalId === this.gameId())),
      error: () => this.ownsGame.set(false)
    });
  }
  

  averageRating = computed(() => {
    const list = this.reviews();
    if (list.length === 0) return '0.0';
    const sum = list.reduce((acc, rev) => acc + rev.rating, 0);
    return (sum / list.length).toFixed(1);
  });

  constructor() {
    effect(() => {
      const id = this.gameId();
      if (id) {
        this.fetchReviews();
      }
    });
    effect(() => {
    this.gameId();
    untracked(() => this.checkOwnerShips());
    });
  }

  fetchReviews(): void {
    this.isLoading.set(true);

    this.reviewService.getReviews({
      gameId: this.gameId(),
      page: this.currentPage(),
      size: 10,
      minRating: this.ratingFilter() ?? undefined,
      maxRating: this.ratingFilter() ?? undefined
    }).subscribe({
      next: (res) => {
        this.totalPages.set(res.totalPages);
        this.totalElements.set(res.totalElements);
        this.reviews.set(res.content);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error fetching reviews:', err);
        this.isLoading.set(false);
      }
    });
  }

  filterAll(): void {
    this.ratingFilter.set(null);
    this.onlyMineFilter.set(false);
    this.currentPage.set(0);
    this.fetchReviews();
  }

  onSelectRating(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.ratingFilter.set(value ? Number(value) : null);
    this.currentPage.set(0);
    this.fetchReviews();
  }

  setRating(value: number): void {
    this.newRating = value;
  }

  toggleForm(): void {
    this.showForm.update(val => !val);
  }

  submitReview(): void {
    const clientUuid = this.currentUserId();
    if (!this.newComment.trim() || !clientUuid) return;

    const request: ReviewRequest = {
      gameExternalId: this.gameId(),
      clientExternalId: this.authService.getClientExternalId() ?? '',
      rating: this.newRating,
      comment: this.newComment
    };

    this.reviewService.createReview(request).subscribe({
      next: () => {
        this.newComment = '';
        this.newRating = 5;
        this.showForm.set(false);
        this.currentPage.set(0);
        this.fetchReviews();
      },
      error: (err) => {
      if (err.status === 409) {
        this.errorMessage.set('Debés tener este juego en tu biblioteca para publicar una reseña.');
      } else {
        this.errorMessage.set('No se pudo publicar la reseña. Intentalo más tarde.');
      }
    }
  });
}

  openReviewDetail(review: Review): void {
    this.selectedReview.set(review);
  }

  closeReviewDetail(): void {
    this.selectedReview.set(null);
  }

  onSaveReview(data: { externalId: string; rating: number; comment: string }): void {
  this.reviewService.updateReview(data.externalId, {
    rating: data.rating,
    comment: data.comment
  }).subscribe({
    next: (updated) => {
      // reemplazo la reseña editada en la lista, sin volver a pedir todo
      this.reviews.update(list =>
        list.map(r => r.externalId === updated.externalId ? updated : r)
      );
      this.closeReviewDetail();
      this.toastService.show('Reseña actualizada');
    },
    error: () => {
      this.toastService.show('No se pudo actualizar la reseña', 'error');
    }
  });
  }

  onDeleteReviewFromModal(externalId: string): void {
    this.deleteReview(externalId);
  }

  deleteReview(externalId: string): void {
    this.reviewService.deleteReview(externalId).subscribe({
      next: () => {
        this.reviews.update(list => list.filter(r => r.externalId !== externalId));
        this.totalElements.update(total => Math.max(0, total - 1));
        this.closeReviewDetail();
        this.toastService.show("Reseña eliminada")
      },
      error: (err) => this.toastService.show("Error al eliminar reseña", err)
    });
  }
}