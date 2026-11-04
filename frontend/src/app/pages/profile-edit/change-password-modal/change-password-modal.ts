import { Component, inject, output, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../services/user-service';
import { UserStateService } from '../../../services/user-state-service';
import { ChangePassword } from '../../../models/change-password';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-change-password-modal',
  styleUrl: './change-password-modal.css',
  templateUrl: './change-password-modal.html',
})
export class ChangePasswordModal {

  close = output<void>()
  errorMessage = signal('')

  passwordForm! : FormGroup
  fb = inject(FormBuilder)
  userService = inject(UserService)
  userStateService = inject(UserStateService)

  constructor(){
    this.loadForm()
  }

  loadForm(){
    this.passwordForm = this.fb.group({
      oldPass : ['', Validators.required],
      newPass : ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,16}$/)]],
      confirmPass : ['', Validators.required]
    })
  }

  passwordsMatch(): boolean {
    return this.passwordForm.get('newPass')?.value === this.passwordForm.get('confirmPass')?.value
  }

  onSubmit(){
    if(this.passwordForm.invalid || !this.passwordsMatch()) return

    const password : ChangePassword = this.passwordForm.value

    if(this.userStateService.client()){

      this.userService.clientChangePassword(password).subscribe({
        next: () => {
          alert('Constraseña cambiada con exito')
          this.onClose()
        },
        error: e => {
          console.error('No se pudo actualizar el password: ', e);

          switch (e.status) {
            case 400:
              this.errorMessage.set('La contraseña actual es incorrecta.');
              break;

            case 422:
              this.errorMessage.set('La nueva contraseña no cumple con el formato requerido.');
              break;

            default:
              this.errorMessage.set('No se pudo actualizar la contraseña. Intentá nuevamente.');
          }
        }
      })
    }else if(this.userStateService.developer()){

      this.userService.developerChangePassword(password).subscribe({
        next: () => {
          alert('Constraseña cambiada con exito')
          this.onClose()
        },
        error: e => {
          console.error('No se pudo actualizar el password: ', e);

          switch (e.status) {
            case 400:
              this.errorMessage.set('La contraseña actual es incorrecta.');
              break;

            case 422:
              this.errorMessage.set('La nueva contraseña no cumple con el formato requerido.');
              break;

            default:
              this.errorMessage.set('No se pudo actualizar la contraseña. Intentá nuevamente.');
          }
        }
      })
    }else if(this.userStateService.admin()){

      this.userService.adminChangePassword(password).subscribe({
        next: () => {
          alert('Constraseña cambiada con exito')
          this.onClose()
        },
        error: e => {
          console.error('No se pudo actualizar el password: ', e);

          switch (e.status) {
            case 400:
              this.errorMessage.set('La contraseña actual es incorrecta.');
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
  }

  onClose(){
    this.close.emit()
  }
}
