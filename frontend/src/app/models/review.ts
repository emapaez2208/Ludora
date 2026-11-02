export interface Review {
  externalId: string;
  rating: number;
  comment: string;
  date: string;
  userName: string;
  gameTitle: string;
  clientExternalId: string;
}

export interface ReviewRequest {
  gameExternalId: string;
  clientExternalId: string;
  rating: number;
  comment: string;
}