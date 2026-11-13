import { Component, inject } from '@angular/core';
import { ToastService } from '../../services/toast-service';
import { ToastMessage } from '../../models/toast-message';
import { Router } from '@angular/router';

@Component({
  imports: [],
  selector: 'app-toast',
  styleUrl: './toast.css',
  templateUrl: './toast.html',
})
export class Toast {
  toastService = inject(ToastService);
  private router = inject(Router);

  open(t: ToastMessage) {
    if (!t.link) return;
    this.router.navigateByUrl(t.link);
    this.toastService.dismiss(t.id);
  }

  onActionClick(t: ToastMessage, event: MouseEvent) {
    event.stopPropagation();
    if (t.action) {
      t.action.fn(); //
      this.toastService.dismiss(t.id);
    }
  }
}
