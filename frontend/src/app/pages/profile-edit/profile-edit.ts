import { Component, effect, inject, signal } from '@angular/core';
import { UserStateService } from '../../services/user-state-service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user-service';
import { ChangePasswordModal } from './change-password-modal/change-password-modal';
import { ChangeEmailModal } from './change-email-modal/change-email-modal';
import { CloudinaryService } from '../../services/cloudinary';

@Component({
  imports: [ReactiveFormsModule, ChangePasswordModal, ChangeEmailModal],
  selector: 'app-profile-edit',
  styleUrl: './profile-edit.css',
  templateUrl: './profile-edit.html',
})
export class ProfileEdit {
  
  userStateService = inject(UserStateService)
  userService = inject(UserService)

  cloudinaryService = inject(CloudinaryService)
  isUploading = signal(false)

  clientForm! : FormGroup
  developerForm! : FormGroup
  adminForm! : FormGroup
  fb = inject(FormBuilder)
  router = inject(Router)
  today = new Date().toLocaleDateString('en-CA')

  showPasswordModal = signal(false)
  showEmailModal = signal(false)

  constructor(){
    this.loadClientForm()
    this.loadDeveloperForm()
    this.loadAdminForm()

    this.effectUsers()
    this.effectDevelopers()
    this.effectAdmin()
  }

  loadClientForm(){

    this.clientForm = this.fb.group({
      avatarUrl: [''],
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      lastName : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      phone : ['', [Validators.required, Validators.min(1000000), Validators.max(99999999999), Validators.pattern(/^[0-9]+$/)]],
      street : ['', [Validators.required, Validators.maxLength(25)]],
      numberStreet : ['', [Validators.required, Validators.min(1), Validators.max(99999), Validators.pattern(/^[0-9]+$/)]],
      birthDate : ['', Validators.required]
    })
  }

  loadDeveloperForm(){
    this.developerForm = this.fb.group({
      avatarUrl: [''],
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      lastName : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      company : ['', Validators.required]
    })
  }

  loadAdminForm(){
    this.adminForm = this.fb.group({
      avatarUrl: [''],
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      lastName : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      employeeId : ['', [Validators.required, Validators.pattern(/^[0-9]+$/), Validators.min(1)]]
    })
  }

  onAvatarSelected(event: Event, form: FormGroup): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const file: File = input.files[0];
      this.isUploading.set(true);

      this.cloudinaryService.uploadAvatar(file).subscribe({
        next: (url: string) => {
          form.patchValue({ avatarUrl: url });
          this.isUploading.set(false);
          input.value = '';
        },
        error: (err) => {
          console.error('Error al subir foto de perfil:', err);
          alert('No se pudo subir la foto de perfil');
          this.isUploading.set(false);
          input.value = '';
        }
      });
    }
  }

  effectUsers(){
    effect(() => {
      const client = this.userStateService.client()
      if(client){
        this.clientForm.patchValue(client)
      }
    })
  }

  effectDevelopers(){
    effect(() => {
      const develop = this.userStateService.developer()
      if(develop){
        this.developerForm.patchValue(develop)
      }
    })
  }

  effectAdmin(){
    effect(() => {
      const admin = this.userStateService.admin()
      if(admin){
        this.adminForm.patchValue(admin)
      }
    })
  }

  onSubmitClient(){

    if (this.clientForm.invalid || this.isUploading()) return;

    this.userService.clientUpdate(this.userStateService.client()!.externalId, this.clientForm.value)
      .subscribe({
        next: () => {
          this.userStateService.loadUser()
          this.router.navigate(['/profile'])
        },
        error: e => {
          console.error('Hubo un error al actualizar ', e)
          alert('No se pudo actualizar con exito')
        }
      })
  }

  onSubmitDeveloper(){
    if(this.developerForm.invalid || this.isUploading()) return;

    this.userService.developerUpdate(this.userStateService.developer()!.externalId, this.developerForm.value)
      .subscribe({
        next: () => {
          this.userStateService.loadUser()
          this.router.navigate(['/profile'])
        },
        error: e => {
          console.error('Hubo un error al actualizar ', e)
          alert('No se pudo actualizar con exito')
        }
      })
  }

  onSubmitAdmin(){
    if(this.adminForm.invalid || this.isUploading()) return;

    this.userService.adminUpdate(this.userStateService.admin()!.externalId, this.adminForm.value)
      .subscribe({
        next: () => {
          this.userStateService.loadUser()
          this.router.navigate(['/profile'])
        },
        error: e => {
          console.error('Hubo un error al actualizar ', e)
          alert('No se pudo actualizar con exito')
        }
      })
  }


  // ------------- MODALES -------------------------- //

  openPasswordModal() {
    this.showPasswordModal.set(true);
  }

  openEmailModal() {
    this.showEmailModal.set(true);
  }

  closePasswordModal() {
    this.showPasswordModal.set(false);
  }

  closeEmailModal() {
    this.showEmailModal.set(false);
  }
}
