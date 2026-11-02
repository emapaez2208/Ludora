import { Component, inject, signal } from '@angular/core';
import { UserService } from '../../services/user-service';
import { RouterLink } from '@angular/router';
import { ClientResponse } from '../../models/client-response';
import { Developer } from '../../models/developer';
import { AdminResponse } from '../../models/admin-response';
import { AuthService } from '../../services/auth-service';

@Component({
  imports: [RouterLink],
  selector: 'app-profile',
  styleUrl: './profile.css',
  templateUrl: './profile.html',
})
export class Profile{

  client = signal<ClientResponse | null>(null)
  developer = signal<Developer | null>(null)
  admin = signal<AdminResponse | null>(null)
  msjError = signal('')

  userService = inject(UserService)
  authService = inject(AuthService)

  constructor(){
    this.loadUser()
  }

  loadUser(){
    if(this.authService.hasRole('ROLE_ADMIN')){
      this.userService.adminProfile().subscribe({
        next: data => this.admin.set(data),
        error: e => {
          console.error('Error al traer al admin', e)
          this.msjError.set('No se pudo cargar el perfil de Administrador')
        }
      })
    }else if(this.authService.hasRole('ROLE_DEVELOPER')){
      this.userService.developerProfile().subscribe({
        next: data => this.developer.set(data),
        error: e => {
          console.error('Error al traer al desarrollador', e)
          this.msjError.set('No se pudo cargar el perfil de Desarrollador')
        }
      })
    }else if(this.authService.hasRole('ROLE_CLIENT')){
      this.userService.clientProfile().subscribe({
        next: data => this.client.set(data),
        error: e => {
          console.error('Error al traer al cliente', e)
          this.msjError.set('No se pudo cargar el perfil de Usuario')
        }
      })
    }
  }

}
