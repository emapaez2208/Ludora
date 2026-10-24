import { HttpClient } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { Genre } from '../models/genre';

@Injectable({
    providedIn: 'root'
})
export class GenreService {

    private http = inject(HttpClient);
    private url = '/api/genres';

    getGenres(): Observable<Genre[]>{
        return this.http.get<Genre[]>(this.url);
    }
}
