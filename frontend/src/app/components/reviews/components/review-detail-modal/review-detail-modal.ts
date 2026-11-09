import { Component, computed, inject, input, output, signal } from '@angular/core';
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
  save = output<{ externalId: string; rating: number; comment: string }>();
  delete = output<string>();

  confirmingDelete = signal(false);

  isEditing = signal(false);
  editRating = 5;
  editComment = '';

  // Evalúa si el usuario autenticado es el autor
  isOwner = computed(() => {
  const me = this.authService.getClientExternalId();
  return !!me && this.review().clientExternalId === me;
  });

  isAdmin = computed(() => !!this.authService.isAdmin());

  canDelete = computed(() => this.isOwner() || this.isAdmin());

  onClose(): void {
    this.close.emit();
  }

  //editar review
  startEdit(): void{
    this.confirmingDelete.set(false);
    this.editRating = this.review().rating;
    this.editComment = this.review().comment;
    this.isEditing.set(true);
  }

  cancelEdit(): void{
    this.isEditing.set(false);
  }

  setEditRating(value : number): void{
    this.editRating = value;
  }

  saveEdit(): void{
    if(!this.editComment.trim()) return;
    this.save.emit({
      externalId: this.review().externalId,
      rating: this.editRating,
      comment: this.editComment
    })
  }

  //eliminar review
  askDelete(): void{
    this.confirmingDelete.set(true);
  }

  cancelDelete(): void{
    this.confirmingDelete.set(false);
  }

  onDelete(): void {
    this.delete.emit(this.review().externalId);
  }
}