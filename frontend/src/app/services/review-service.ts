import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Review, ReviewRequest, ReviewSummary, ReviewUpdate } from '../models/review';
import { PageResponse } from '../models/page-response';

export interface ReviewFilterParams {
  page: number;
  size: number;
  gameId?: string;
  clientId?: string;
  userName?: string;
  minRating?: number;
  maxRating?: number;
  minDate?: string;
  maxDate?: string;
  sortBy?: string;
  sortDirection?: string;
}
@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private http = inject(HttpClient);
  
  private readonly baseUrl = '/api/reviews';

  getReviews(filters: ReviewFilterParams): Observable<PageResponse<Review>> {
    let params = new HttpParams()
      .set('page', filters.page.toString())
      .set('size', filters.size.toString());

    if (filters.gameId) params = params.set('gameId', filters.gameId);
    if (filters.clientId) params = params.set('clientId', filters.clientId);
    if (filters.minRating != null) params = params.set('minRating', filters.minRating.toString());
    if (filters.maxRating != null) params = params.set('maxRating', filters.maxRating.toString());
    if (filters.minDate) params = params.set('minDate', filters.minDate);
    if (filters.maxDate) params = params.set('maxDate', filters.maxDate);
    if (filters.userName) params = params.set('userName', filters.userName);
    if (filters.sortBy) params = params.set('sortBy', filters.sortBy);
    if (filters.sortDirection) params = params.set('sortDirection', filters.sortDirection);

    return this.http.get<PageResponse<Review>>(this.baseUrl, { params });
  }
  getReviewsByGameId(gameId: string, page: number = 0, size: number = 5): Observable<PageResponse<Review>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<Review>>(`${this.baseUrl}/games/${gameId}`, { params });
  }

  createReview(review : ReviewRequest): Observable<Review> {
    return this.http.post<Review>(this.baseUrl, review);
  }

  updateReview(reviewId: string, body: ReviewUpdate): Observable<Review> {
  return this.http.put<Review>(`${this.baseUrl}/${reviewId}`, body);
}

  deleteReview(reviewId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${reviewId}`);
  }

  getSummary(gameId: string): Observable<ReviewSummary> {
  return this.http.get<ReviewSummary>(`${this.baseUrl}/games/${gameId}/summary`);
}
}