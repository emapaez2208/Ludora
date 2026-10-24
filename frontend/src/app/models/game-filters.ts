export interface GameFilters {
    name?: string;
    minPrice?: number | null;
    maxPrice?: number | null;
    minReleaseDate?: string | null;
    maxReleaseDate?: string | null;
    genreNames?: string[];
    rangeName?: string | null;
    developerCompany?: string | null;
}
