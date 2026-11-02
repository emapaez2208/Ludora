import { Component, computed, inject, input, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Review } from '../../../../models/review';
import { AuthService } from '../../../../services/auth-service';

@Component({
  selector: 'app-review-detail-modal',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './review-detail-modal.html',
  styleUrl: './review-detail-modal.css',
})
export class ReviewDetailModalComponent {
  private authService = inject(AuthService);

  review = input.required<Review>();

  close = output<void>();
  edit = output<Review>();
  delete = output<string>();

  // Evalúa si el usuario autenticado es el autor
  isOwner = computed(() => {
  const me = this.authService.getUsername();
  return !!me && this.review().userName === me;
});

  onClose(): void {
    this.close.emit();
  }

  onEdit(): void {
    this.edit.emit(this.review());
  }

  onDelete(): void {
    this.delete.emit(this.review().externalId);
  }
}