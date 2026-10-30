import { Component, effect, inject } from '@angular/core';
import { UserStateService } from '../../services/user-state-service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user-service';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-profile-edit',
  styleUrl: './profile-edit.css',
  templateUrl: './profile-edit.html',
})
export class ProfileEdit {
  
  userStateService = inject(UserStateService)
  userService = inject(UserService)

  clientForm! : FormGroup
  fb = inject(FormBuilder)
  router = inject(Router)
  today = new Date().toLocaleDateString('en-CA')

  constructor(){
    this.loadClientForm()
    this.effectUsers()
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

  effectUsers(){
    effect(() => {
      const client = this.userStateService.client()
      if(client){
        this.clientForm.patchValue(client)
      }
    })
  }

  onSubmitClient(){

    if(this.clientForm.invalid) return

    this.userService.clientUpdate(this.userStateService.client()!.externalId, this.clientForm.value)
      .subscribe({
        next: () => {
          alert('Usuario actualizado con exito')
          this.userStateService.loadUser()
          this.router.navigate(['/profile'])
        },
        error: e => {
          console.error('Hubo un error al actualizar ', e)
          alert('No se pudo actualizar con exito')
        }
      })
  }
}
