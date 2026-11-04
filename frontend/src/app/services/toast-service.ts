import { Injectable, signal } from '@angular/core';
import { ToastMessage } from '../models/toast-message';

@Injectable({ 
    providedIn: 'root' }
)
export class ToastService {
    private nextId = 0;
    toasts = signal<ToastMessage[]>([]);

    show(message: string, type: ToastMessage['type'] = 'success', ms = 3000, link?: string, action?: { label: string; fn: () => void }) {
        const id = this.nextId++;
        this.toasts.update(t => [...t, { id, message, type, link, action }]);
        setTimeout(() => this.dismiss(id), ms);
    }

    dismiss(id: number) {
        this.toasts.update(t => t.filter(x => x.id !== id));
    }
}
