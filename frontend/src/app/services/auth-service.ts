import { HttpClient } from '@angular/common/http';
import { inject, Service, signal } from '@angular/core';
import { Auth } from '../models/auth';
import { AuthResponse } from '../models/auth-response';
import { map } from 'rxjs';

@Service()
export class AuthService {

    private readonly url = '/api/auth'
    private http = inject(HttpClient)

    // Signal reactivo: se inicializa según el token que ya exista
    private loggedIn = signal<boolean>(this.checkToken());
    readonly isLoggedIn$ = this.loggedIn.asReadonly();

    private checkToken(): boolean {
        const payload = this.getPayload();
        return !!payload && payload.exp * 1000 > Date.now();
    }
    
    login(auth : Auth){
        return this.http.post<AuthResponse>(`${this.url}/login`, auth).pipe(
            map(auth => this.setToken(auth.token)))
    }

    setToken(token: string): void {
        localStorage.setItem('token', token);
        this.loggedIn.set(true);
    }

    logout(): void {
        localStorage.removeItem('token');
        this.loggedIn.set(false);
    }


    getToken(){
        return localStorage.getItem('token')
    }

    private getPayload() : any | null{
        const token = this.getToken()

        if(!token) return null

        try {
            return JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
        } catch {
            return null;
        }
    }

    isLoggedIn(): boolean {
    const ok = this.checkToken();
    if (this.loggedIn() !== ok) this.loggedIn.set(ok);
    return ok;
  }

    getRoles(): string[] {
        return this.getPayload()?.roles ?? [];
    }

    hasRole(role: string): boolean {
        return this.getRoles().includes(role);
    }

}
