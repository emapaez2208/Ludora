import { Component, inject, input, output, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../services/user-service';
import { AdminChangeEmail } from '../../../models/admin-change-email';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-admin-change-email-modal',
  styleUrl: './admin-change-email-modal.css',
  templateUrl: './admin-change-email-modal.html',
})
export class AdminChangeEmailModal {

  // externalId del usuario al que se le cambia el email
  externalId = input.required<string>()

  close = output<void>()
  // Emite el nuevo email cuando el backend confirma el cambio
  updated = output<string>()
  errorMessage = signal('')

  emailForm! : FormGroup
  fb = inject(FormBuilder)
  userService = inject(UserService)

  constructor(){
    this.loadForm()
  }

  loadForm(){
    this.emailForm = this.fb.group({
      newEmail : ['', [Validators.required, Validators.pattern(/^[A-Z0-9._%+-]+@(?:[A-Z0-9-]+\.)+[A-Z]{2,}$/i)]],
      confirmEmail : ['', Validators.required]
    })
  }

  emailsMatch(){
    return this.emailForm.get('newEmail')?.value === this.emailForm.get('confirmEmail')?.value
  }

  onSubmit(){
    if(this.emailForm.invalid || !this.emailsMatch()) return

    const body : AdminChangeEmail = { newEmail: this.emailForm.value.newEmail }

    this.userService.adminChangeUserEmail(this.externalId(), body).subscribe({
      next: () => {
        alert('Email actualizado con exito')
        this.updated.emit(body.newEmail)
        this.onClose()
      },
      error: e => {
        console.error('Error al actualizar el email :', e)

        switch(e.status){
          case 404:
            this.errorMessage.set('El usuario no fue encontrado.')
            break

          case 409:
            this.errorMessage.set('El nuevo email ya corresponde a otro usuario, ingrese otro.')
            break

          case 422:
            this.errorMessage.set('El formato del email no es válido.')
            break

          default:
            this.errorMessage.set('No se pudo actualizar el email. Intentá nuevamente.')
            break
        }
      }
    })
  }

  onClose(){
    this.close.emit()
  }
}