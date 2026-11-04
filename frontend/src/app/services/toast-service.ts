import { Injectable, signal } from '@angular/core';
import { ToastMessage } from '../models/toast-message';

@Injectable({ 
    providedIn: 'root' }
)
export class ToastService {
    private nextId = 0;
    toasts = signal<ToastMessage[]>([]);

    show(message: string, type: ToastMessage['type'] = 'success', ms = 3000, link?: string) {
        const id = this.nextId++;
        this.toasts.update(t => [...t, { id, message, type, link }]);
        setTimeout(() => this.dismiss(id), ms);
    }

    dismiss(id: number) {
        this.toasts.update(t => t.filter(x => x.id !== id));
    }
}
