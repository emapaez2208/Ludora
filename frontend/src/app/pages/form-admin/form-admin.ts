import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { AdminRequest } from '../../models/admin-request';
import { Router } from '@angular/router';
import { UserService } from '../../services/user-service';

function passwordsIguales(group: AbstractControl): ValidationErrors | null {
  return group.get('password')?.value === group.get('password2')?.value
    ? null
    : { passwordsDistintas: true };
}

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-form-admin',
  styleUrl: './form-admin.css',
  templateUrl: './form-admin.html',
})
export class FormAdmin {

  adminForm! : FormGroup;
   // Signal para controlar la alerta visual
  alerta = signal<{ tipo: 'exito' | 'error'; mensaje: string } | null>(null);
  enviando = signal(false);

  private fb = inject(FormBuilder)
  private router = inject(Router)
  private userService = inject(UserService)


  constructor(){

   this.adminForm = this.fb.group({
    name:  ['', [Validators.required , Validators.maxLength(32), Validators.minLength(3)]],
    lastName: ['', [Validators.required , Validators.maxLength(32), Validators.minLength(3)]],
    userName: ['', [Validators.required , Validators.maxLength(20) , Validators.minLength(5)]] ,
    email: ['', [Validators.required , Validators.email,Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)]],
    password: ['', [ Validators.required,  Validators.minLength(8),  Validators.maxLength(16), 
                       Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,16}$/)]],
    password2: ['', Validators.required],
    employeeId: ['', [Validators.required, Validators.min(1) ]]
}, { validators: passwordsIguales });
}

onSubmit() {
  
  // Evita doble envío (también protege si se dispara Enter dos veces)
  if (this.enviando() || this.adminForm.invalid) return;
  this.enviando.set(true);

  const {password2 , ...adminData} = this.adminForm.value;

  const formattedData = { ...adminData,
                         employeeId : Number (adminData.employeeId)
  
  };

  this.userService.addAdmin(formattedData as AdminRequest)
  .subscribe({
    next: (data) => { console.log('Administrador cargado con éxito',data),
                      this.alerta.set({
                          tipo: 'exito',
                          mensaje: 'El administrador se ha registrado con éxito'
                          });
                      setTimeout(()=>
                      this.router.navigate(['/']),3000)},
    error: (e) => { console.log('Error al registrar un administrador', e),
                     this.alerta.set({
                        tipo: 'error',
                        mensaje: e.error?.message || 'Ocurrió un error al registrar.'
                        });
                        this.enviando.set(false); 
    }
  });

}
}
