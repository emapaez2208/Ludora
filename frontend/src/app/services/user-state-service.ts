import { effect, inject, Service, signal } from '@angular/core';
import { ClientResponse } from '../models/client-response';
import { Developer } from '../models/developer';
import { AdminResponse } from '../models/admin-response';
import { UserService } from './user-service';
import { AuthService } from './auth-service';

@Service()
export class UserStateService {

    client = signal<ClientResponse | null>(null)
    developer = signal<Developer | null>(null)
    admin = signal<AdminResponse | null>(null)
    msjError = signal('')
    loaded = signal(false)

    userService = inject(UserService)
    authService = inject(AuthService)

    constructor(){
        effect(() => {

            if(this.authService.isLoggedIn$()){
                this.loadUser()
            }else{
                this.clearUser()
            } 
        })
        
    }

    loadUser(){
        this.clearUser()

        if(this.authService.hasRole('ROLE_ADMIN')){
        this.userService.adminProfile().subscribe({
            next: data => {
                this.admin.set(data)
                this.loaded.set(true)
            },
            error: e => {
            console.error('Error al traer al admin', e)
            this.msjError.set('No se pudo cargar el perfil de Administrador')
            this.loaded.set(true)
            }
        })
        }else if(this.authService.hasRole('ROLE_DEVELOPER')){
        this.userService.developerProfile().subscribe({
            next: data => {
                this.developer.set(data)
                this.loaded.set(true)
            },
            error: e => {
            console.error('Error al traer al desarrollador', e)
            this.msjError.set('No se pudo cargar el perfil de Desarrollador')
            this.loaded.set(true)
            }
        })
        }else if(this.authService.hasRole('ROLE_CLIENT')){
        this.userService.clientProfile().subscribe({
            next: data => {
                this.client.set(data)
                this.loaded.set(true)
            },
            error: e => {
            console.error('Error al traer al cliente', e)
            this.msjError.set('No se pudo cargar el perfil de Usuario')
            this.loaded.set(true)
            }
        })
        }else{
            this.loaded.set(true)
        }
    }

    clearUser() {
        this.client.set(null);
        this.developer.set(null);
        this.admin.set(null);
        this.msjError.set('');
        this.loaded.set(false);
    }
}
