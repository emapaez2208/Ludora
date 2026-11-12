import { Component, inject, input, output, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../services/user-service';
import { AdminChangePassword } from '../../../models/admin-change-password';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-admin-change-password-modal',
  styleUrl: './admin-change-password-modal.css',
  templateUrl: './admin-change-password-modal.html',
})
export class AdminChangePasswordModal {

  // externalId del usuario al que se le cambia la contraseña
  externalId = input.required<string>()

  close = output<void>()
  errorMessage = signal('')

  passwordForm! : FormGroup
  fb = inject(FormBuilder)
  userService = inject(UserService)

  constructor(){
    this.loadForm()
  }

  loadForm(){
    this.passwordForm = this.fb.group({
      newPass : ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,16}$/)]],
      confirmPass : ['', Validators.required]
    })
  }

  passwordsMatch(): boolean {
    return this.passwordForm.get('newPass')?.value === this.passwordForm.get('confirmPass')?.value
  }

  onSubmit(){
    if(this.passwordForm.invalid || !this.passwordsMatch()) return

    const body : AdminChangePassword = { newPass: this.passwordForm.value.newPass }

    this.userService.adminChangeUserPassword(this.externalId(), body).subscribe({
      next: () => {
        alert('Contraseña cambiada con exito')
        this.onClose()
      },
      error: e => {
        console.error('No se pudo actualizar el password: ', e);

        switch (e.status) {
          case 404:
            this.errorMessage.set('El usuario no fue encontrado.');
            break;

          case 422:
            this.errorMessage.set('La nueva contraseña no cumple con el formato requerido.');
            break;

          default:
            this.errorMessage.set('No se pudo actualizar la contraseña. Intentá nuevamente.');
        }
      }
    })
  }

  onClose(){
    this.close.emit()
  }
}