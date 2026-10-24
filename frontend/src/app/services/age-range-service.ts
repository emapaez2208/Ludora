import { HttpClient } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { AgeRange } from '../models/age-range';

@Injectable(
    { providedIn: 'root' }
)
export class AgeRangeService {
    private http = inject(HttpClient);
    private url = '/api/ageRanges';

    getAgeRanges(): Observable<AgeRange[]>{
        return this.http.get<AgeRange[]>(this.url);
    }
}
