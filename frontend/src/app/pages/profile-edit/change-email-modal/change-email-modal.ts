import { Component, inject, output, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../services/user-service';
import { UserStateService } from '../../../services/user-state-service';
import { ChangeEmail } from '../../../models/change-email';
import { startWith } from 'rxjs';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-change-email-modal',
  styleUrl: './change-email-modal.css',
  templateUrl: './change-email-modal.html',
})
export class ChangeEmailModal {

  close = output<void>()
  errorMessage = signal('')

  emailForm! : FormGroup
  fb = inject(FormBuilder)
  userService = inject(UserService)
  userStateService = inject(UserStateService)

  constructor(){
    this.loadForm()
  }

  loadForm(){
    this.emailForm = this.fb.group({
          oldEmail : ['', [Validators.required, Validators.pattern(/^[A-Z0-9._%+-]+@(?:[A-Z0-9-]+\.)+[A-Z]{2,}$/i)]],
          newEmail : ['', [Validators.required, Validators.pattern(/^[A-Z0-9._%+-]+@(?:[A-Z0-9-]+\.)+[A-Z]{2,}$/i)]],
          confirmEmail : ['', Validators.required]
        })
  }

  emailsMatch(){
    return this.emailForm.get('newEmail')?.value === this.emailForm.get('confirmEmail')?.value
  }

  onSubmit(){
    
    if(this.emailForm.invalid || !this.emailsMatch()) return

    const email : ChangeEmail = this.emailForm.value

    if(this.userStateService.client()){

      this.userService.clientChangeEmail(email).subscribe({
        next: () => {
          alert('Email actualizado con exito')
          this.onClose()
        },
        error: e => {
          console.error('Error al actualizar el email :', e)
        
          switch(e.status){
            case 404:
              this.errorMessage.set('El usuario no fue encontrado, inicie sesion nuevamente.')
              break

            case 409:
              this.errorMessage.set('El nuevo email ya corresponde a otro usuario, ingrese otro.')
              break

            case 422:
              this.errorMessage.set('El email actual ingresado no es correcto.')
              break

            default:
              this.errorMessage.set('No se pudo actualizar el email. Intentá nuevamente.')
              break
          }
        }
      })
    }else if(this.userStateService.developer()){

      this.userService.developerChangeEmail(email).subscribe({
        next: () => {
          alert('Email actualizado con exito')
          this.onClose()
        },
        error: e => {
          console.error('Error al actualizar el email :', e)
        
          switch(e.status){
            case 404:
              this.errorMessage.set('El usuario no fue encontrado, inicie sesion nuevamente.')
              break

            case 409:
              this.errorMessage.set('El nuevo email ya corresponde a otro usuario, ingrese otro.')
              break

            case 422:
              this.errorMessage.set('El email actual ingresado no es correcto.')
              break

            default:
              this.errorMessage.set('No se pudo actualizar el email. Intentá nuevamente.')
              console.error('error email: ', e.status)
              break
          }
        }
      })
    }else if(this.userStateService.admin()){
      
      this.userService.adminChangeEmail(email).subscribe({
        next: () => {
          alert('Email actualizado con exito')
          this.onClose()
        },
        error: e => {
          console.error('Error al actualizar el email :', e)
        
          switch(e.status){
            case 404:
              this.errorMessage.set('El usuario no fue encontrado, inicie sesion nuevamente.')
              break

            case 409:
              this.errorMessage.set('El nuevo email ya corresponde a otro usuario, ingrese otro.')
              break

            case 422:
              this.errorMessage.set('El email actual ingresado no es correcto.')
              break

            default:
              this.errorMessage.set('No se pudo actualizar el email. Intentá nuevamente.')
              break
          }
        }
      })
    }
  }

  onClose(){
    this.close.emit()
  }
}
