export interface ToastMessage {
    id: number;
    message: string;
    type: 'success' | 'error' | 'info';
    link?: string;
    action?: { 
        label: string; 
        fn: () => void 
    }
}
