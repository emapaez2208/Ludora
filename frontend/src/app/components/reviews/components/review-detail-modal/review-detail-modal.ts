import { Component, computed, inject, input, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Review } from '../../../../models/review';
import { UserStateService } from '../../../../services/user-state-service';

@Component({
  selector: 'app-review-detail-modal',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './review-detail-modal.html',
  styleUrl: './review-detail-modal.css',
})
export class ReviewDetailModalComponent {
  private userStateService = inject(UserStateService)

  review = input.required<Review>();

  close = output<void>();
  edit = output<Review>();
  delete = output<string>();

  // Evalúa si el usuario autenticado es el autor
  isOwner = computed(() => {
  const me = this.userStateService.client()?.externalId;
  return !!me && this.review().clientExternalId === me;
  });

  isAdmin = computed(() => !!this.userStateService.admin());

  canDelete = computed(() => this.isOwner() || this.isAdmin());

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