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

export interface ReviewUpdate {
  rating: number;
  comment: string;
}

export interface ReviewSummary {
  average: number;
  total: number;
}