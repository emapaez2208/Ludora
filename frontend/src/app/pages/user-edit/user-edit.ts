import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user-service';
import { CloudinaryService } from '../../services/cloudinary';
import { ClientResponse } from '../../models/client-response';
import { Developer } from '../../models/developer';
import { AdminChangeEmailModal } from './admin-change-email-modal/admin-change-email-modal';
import { AdminChangePasswordModal } from './admin-change-password-modal/admin-change-password-modal';

@Component({
  imports: [ReactiveFormsModule, AdminChangeEmailModal, AdminChangePasswordModal],
  selector: 'app-user-edit',
  styleUrl: './user-edit.css',
  templateUrl: './user-edit.html',
})
export class UserEdit implements OnInit {

  private route = inject(ActivatedRoute)
  private router = inject(Router)
  private fb = inject(FormBuilder)
  private userService = inject(UserService)
  private cloudinaryService = inject(CloudinaryService)

  readonly listUrl = '/management/users'

  externalId = ''
  today = new Date().toLocaleDateString('en-CA')

  // Usuario que se está editando (solo uno de los dos tiene valor)
  client = signal<ClientResponse | null>(null)
  developer = signal<Developer | null>(null)
  msjError = signal('')
  isUploading = signal(false)
  showEmailModal = signal(false)
  showPasswordModal = signal(false)

  clientForm!: FormGroup
  developerForm!: FormGroup

  constructor() {
    this.loadClientForm()
    this.loadDeveloperForm()
  }

  ngOnInit(): void {
    this.externalId = this.route.snapshot.paramMap.get('externalId') ?? ''
    this.loadUser()
  }

  // ------------- FORMULARIOS (mismas validaciones que profile-edit) ------------- //

  loadClientForm() {
    this.clientForm = this.fb.group({
      avatarUrl: [''],
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      lastName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      phone: ['', [Validators.required, Validators.min(1000000), Validators.max(99999999999), Validators.pattern(/^[0-9]+$/)]],
      street: ['', [Validators.required, Validators.maxLength(25)]],
      numberStreet: ['', [Validators.required, Validators.min(1), Validators.max(99999), Validators.pattern(/^[0-9]+$/)]],
      birthDate: ['', Validators.required]
    })
  }

  loadDeveloperForm() {
    this.developerForm = this.fb.group({
      avatarUrl: [''],
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      lastName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      company: ['', Validators.required]
    })
  }

  // ------------- CARGA DEL USUARIO ------------- //
  // La ruta solo trae el externalId, así que se prueba primero como cliente
  // y, si el backend responde 404, como developer. y si no , no existe XD
  // otra idea es recibida ?

  private loadUser() {
    if (!this.externalId) {
      this.msjError.set('No se indicó el usuario a editar.')
      return
    }

    this.userService.getClientById(this.externalId).subscribe({
      next: client => {
        this.client.set(client)
        this.clientForm.patchValue(client)
      },
      error: e => {
        if (e.status === 404) {
          this.loadDeveloper()
        } else {
          console.error('Error al traer al cliente', e)
          this.msjError.set(`No se pudo cargar el usuario (código ${e.status ?? 'sin respuesta'}).`)
        }
      }
    })
  }

  private loadDeveloper() {
    this.userService.getDeveloperById(this.externalId).subscribe({
      next: developer => {
        this.developer.set(developer)
        this.developerForm.patchValue(developer)
      },
      error: e => {
        console.error('Error al traer al desarrollador', e)
        this.msjError.set(
          e.status === 404
            ? 'El usuario no fue encontrado.'
            : `No se pudo cargar el usuario (código ${e.status ?? 'sin respuesta'}).`
        )
      }
    })
  }

  // ------------- AVATAR ------------- //

  onAvatarSelected(event: Event, form: FormGroup): void {
    const input = event.target as HTMLInputElement

    if (input.files && input.files.length > 0) {
      const file: File = input.files[0]
      this.isUploading.set(true)

      this.cloudinaryService.uploadAvatar(file).subscribe({
        next: (url: string) => {
          form.patchValue({ avatarUrl: url })
          this.isUploading.set(false)
          input.value = ''
        },
        error: (err) => {
          console.error('Error al subir foto de perfil:', err)
          alert('No se pudo subir la foto de perfil')
          this.isUploading.set(false)
          input.value = ''
        }
      })
    }
  }

  // ------------- GUARDAR ------------- //

  onSubmitClient() {
    if (this.clientForm.invalid || this.isUploading()) return

    this.userService.clientUpdate(this.externalId, this.clientForm.value).subscribe({
      next: () => this.goBack(),
      error: e => {
        console.error('Hubo un error al actualizar ', e)
        alert('No se pudo actualizar con exito')
      }
    })
  }

  onSubmitDeveloper() {
    if (this.developerForm.invalid || this.isUploading()) return

    this.userService.developerUpdate(this.externalId, this.developerForm.value).subscribe({
      next: () => this.goBack(),
      error: e => {
        console.error('Hubo un error al actualizar ', e)
        alert('No se pudo actualizar con exito')
      }
    })
  }

  // ------------- MODALES EMAIL / PASSWORD ------------- //
  // Para mantener la misma idea que perfilEdit

  openEmailModal() { this.showEmailModal.set(true) }
  closeEmailModal() { this.showEmailModal.set(false) }
  openPasswordModal() { this.showPasswordModal.set(true) }
  closePasswordModal() { this.showPasswordModal.set(false) }

  // Refleja en pantalla el email nuevo
  onEmailUpdated(email: string) {
    this.client.update(c => c ? { ...c, email } : c)
    this.developer.update(d => d ? { ...d, email } : d)
  }

  goBack() {
    this.router.navigate([this.listUrl])
  }
}