import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DeveloperService {
  private http = inject(HttpClient);
  private apiUrl = '/api/developers';

  getCompanies(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/companies`);
  }
}