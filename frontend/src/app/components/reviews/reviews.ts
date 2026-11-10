import { Component, computed, effect, inject, input, signal, untracked } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { ReviewService } from '../../services/review-service';
import { Review, ReviewRequest, ReviewSummary } from '../../models/review';
import { AuthService } from '../../services/auth-service';
import { ReviewDetailModalComponent } from './components/review-detail-modal/review-detail-modal';
import { GameService } from '../../services/game-service';
import { ToastService } from '../../services/toast-service';
import { ReviewFilters, ReviewSortOptions } from '../../models/review-filters';
import { ReviewFilterPanel } from './components/review-filter-panel/review-filter-panel';
import { Pagination } from '../pagination/pagination';
import { ReviewSort } from './components/review-sort/review-sort';

@Component({
  selector: 'app-reviews',
  standalone: true,
  imports: [FormsModule, 
            DatePipe, 
            ReviewDetailModalComponent,
            ReviewFilterPanel,
            ReviewSort,
            Pagination],
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

  filters = signal<ReviewFilters>({});
  sort = signal<ReviewSortOptions>({ sortBy: 'date', sortDirection: 'desc' });
  showFilters = signal<boolean>(false);

   hasActiveFilters = computed(() => {
    const f = this.filters();
    return f.minRating != null || f.maxRating != null || !!f.minDate
        || !!f.maxDate || !!f.userName;
  });

  private today = new Date().toLocaleDateString('en-CA');

  showForm = signal<boolean>(false);
  newRating: number = 5;
  newComment: string = '';

  ownsGame = signal<boolean>(false) //booleano para determinar si el viewer de la reseña tiene el juego adquirido

  //guarda la reseña propia
  myReview = signal<Review | null>(null);
  userHasReviewed = computed(() => !!this.myReview());

  summary = signal<ReviewSummary>({ average: 0, total: 0 });
  averageRating = computed(() => this.summary().average.toFixed(1));

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

  loadMyReview(): void{
    const myId = this.currentUserId();
    if (!this.isClient() || !myId){
      this.myReview.set(null);
      return;
    }

    this.reviewService.getReviews({ gameId: this.gameId(), clientId: myId, page: 0, size: 1 }).subscribe({
      next: (res) => this.myReview.set(res.content[0] ?? null),
      error: () => this.myReview.set(null)
    });  
  }

  loadSummary(): void{
    this.reviewService.getSummary(this.gameId()).subscribe({
      next: (s) => this.summary.set(s),
      error:() => this.summary.set({average:0, total: 0})
    });
  }

  constructor() {
    effect(() => {
      if (this.gameId()) {
        this.fetchReviews();
      }
    });
    effect(() => {
      this.gameId();
      untracked(() => {
        this.checkOwnerShips();
        this.loadMyReview();
        this.loadSummary();
      });
    });
  }

  fetchReviews(): void {
    this.isLoading.set(true);

    const f = this.filters();
    const s = this.sort();

    this.reviewService.getReviews({
      gameId: this.gameId(),
      page: this.currentPage(),
      size: 10,
      minRating: f.minRating ?? undefined,
      maxRating: f.maxRating ?? undefined,
      minDate: f.minDate ? this.startOfDay(f.minDate) : undefined,
      maxDate: f.maxDate ? this.endOfDay(f.maxDate) : undefined,
      userName: f.userName?.trim() || undefined,
      sortBy: s.sortBy,
      sortDirection: s.sortDirection
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

    private startOfDay(date: string): string {
    return `${date}T00:00:00`;
  }
    private endOfDay(date: string): string {
    // el back rechaza fechas futuras, si es hoy, mando la hora actual
    if (date === this.today) {
      return new Date().toLocaleString('sv-SE').replace(' ', 'T');
    }
    return `${date}T23:59:59`;
  }

  //filtros, orden y paginacion
    toggleFilters(): void {
    this.showFilters.update(v => !v);
  }

  onApplyFilters(f: ReviewFilters): void {
    this.filters.set(f);
    this.currentPage.set(0);
    this.showFilters.set(false);
  }

  onSortChange(s: ReviewSortOptions): void {
    this.sort.set(s);
    this.currentPage.set(0);
  }

  goToPage(page: number): void {
    this.currentPage.set(page);
  }

  //alta de review
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
      clientExternalId: clientUuid,
      rating: this.newRating,
      comment: this.newComment
    };

    this.reviewService.createReview(request).subscribe({
      next: () => {
        this.newComment = '';
        this.newRating = 5;
        this.showForm.set(false);
        this.loadMyReview();
        this.loadSummary();
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

  //detalle modal / edicion y eliminar
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
        if (this.myReview()?.externalId === updated.externalId) {
          this.myReview.set(updated);
        }
      this.loadSummary();
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
        if (this.myReview()?.externalId === externalId) {
          this.myReview.set(null);
        }
        this.loadSummary();
        this.closeReviewDetail();
        this.toastService.show('Reseña eliminada');
      },
      error: () => this.toastService.show('No se pudo eliminar la reseña', 'error')
    });
  }
}