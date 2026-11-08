import { SaleItem } from './sale-item';
 
export type SaleStatus = 'PENDING' | 'APPROVED' | 'REJECTED';
 
export interface SaleResponse {
  externalId: string;
  date: string;
  status: SaleStatus;
  totalPrice: number;
  earnedPoints: number;
  hasDiscount: boolean;
  userName: string;
  items: SaleItem[];
}
 