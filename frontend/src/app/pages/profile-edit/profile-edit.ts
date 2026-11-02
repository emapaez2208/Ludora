import { Component, effect, inject, signal } from '@angular/core';
import { UserStateService } from '../../services/user-state-service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user-service';
import { ChangePasswordModal } from './change-password-modal/change-password-modal';

@Component({
  imports: [ReactiveFormsModule, ChangePasswordModal],
  selector: 'app-profile-edit',
  styleUrl: './profile-edit.css',
  templateUrl: './profile-edit.html',
})
export class ProfileEdit {
  
  userStateService = inject(UserStateService)
  userService = inject(UserService)

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
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      lastName : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      company : ['', Validators.required]
    })
  }

  loadAdminForm(){
    this.adminForm = this.fb.group({
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      lastName : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]],
      employeeId : ['', [Validators.required, Validators.pattern(/^[0-9]+$/), Validators.min(1)]]
    })
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

    if(this.clientForm.invalid) return

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
    if(this.developerForm.invalid) return

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
    if(this.adminForm.invalid) return

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
