export interface ReviewFilters {
  minRating?: number | null;
  maxRating?: number | null;
  minDate?: string | null;
  maxDate?: string | null;
  userName?: string | null;
}

export interface ReviewSortOptions {
  sortBy: 'date' | 'rating';
  sortDirection: 'asc' | 'desc';
}