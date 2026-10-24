import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DeveloperService {

    private http = inject(HttpClient);

    getCompanies(): Observable<string[]>{
        return this.http.get<string[]>('/api/developers/companies');
    }
}
