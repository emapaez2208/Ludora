import { Component, effect, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user-service';
import { ClientRequest } from '../../models/client-request';
import { DeveloperRequest } from '../../models/developer-request';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { AuthService } from '../../services/auth-service';

function passwordsIguales(group: AbstractControl): ValidationErrors | null {
  return group.get('password')?.value === group.get('password2')?.value
    ? null
    : { passwordsDistintas: true };
}

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-register',
  styleUrl: './register.css',
  templateUrl: './register.html',
})
export class Register  {

  userForm!: FormGroup;
  maxDate!: string;

  user = signal<'client' | 'develop' >('client');
  role!: string

  // Signal para controlar la alerta visual
  alerta = signal<{ tipo: 'exito' | 'error'; mensaje: string } | null>(null);
  enviando = signal(false);

  private fb = inject(FormBuilder)
  private router = inject(Router)
  private authService = inject(AuthService)

  constructor(){

    this.maxDate = new Date().toISOString().split('T')[0];

    effect(()=>{
  
    const roleSelecionado = this.user();
    this.buildForm(roleSelecionado);
    })
  }


   private buildForm( role : 'client' | 'develop'){
    if(role == 'client'){
      this.userForm = this.fb.group({
      name:  ['', [Validators.required , Validators.maxLength(32), Validators.minLength(3)]],
      lastName: ['', [Validators.required , Validators.maxLength(32), Validators.minLength(3)]],
      userName: ['', [Validators.required , Validators.maxLength(20) , Validators.minLength(5)]] ,
      email: ['', [Validators.required , Validators.email,Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16), 
                      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,16}$/)]],
      password2: ['', Validators.required],
      phone: ['', [Validators.required , Validators.min(1000000) , Validators.max(99999999999)]],
      street: ['',[ Validators.required , Validators.maxLength(25)]],
      numberStreet: ['', [Validators.required , Validators.min(1) , Validators.max(99999) , Validators.pattern('^[0-9]+$')]],
      birthDate: ['',[Validators.required, this.pastDateValidator()] ]
    }, { validators: passwordsIguales });
  } else{
       this.userForm = this.fb.group({
      name:  ['', [Validators.required , Validators.maxLength(32), Validators.minLength(3)]],
      lastName: ['', [Validators.required , Validators.maxLength(32), Validators.minLength(3)]],
      userName: ['', [Validators.required , Validators.maxLength(20) , Validators.minLength(5)]] ,
      email: ['', [Validators.required , Validators.email,Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)]],
      password: ['', [ Validators.required,  Validators.minLength(8),  Validators.maxLength(16), 
                       Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,16}$/)]],
      password2: ['', Validators.required],
      company: ['' , Validators.required]
    }, { validators: passwordsIguales });
  }

}


  onSubmit(){

    if (this.enviando() || this.userForm.invalid) return;
    this.enviando.set(true);

    if(this.user() ==='client'){
    const {password2, ...clientData} = this.userForm.value;

    const formattedData = {
      ...clientData,
      phone: Number(clientData.phone),
      numberStreet: Number(clientData.numberStreet)
    };

    this.authService.addUser(formattedData as ClientRequest).subscribe({
      next: (data)=> {
        this.alerta.set({
          tipo: 'exito',
          mensaje: 'Usted se ha registrado con éxito'
        });
        console.log('Usuario cargado con éxito', data);

        setTimeout(()=>
        this.router.navigate(['/login']),3000)},
      error: (err) => {
        console.error('Error al registrar el usuario', err);
       this.alerta.set({
          tipo: 'error',
          mensaje: err.error?.message || 'Ocurrió un error al registrarse.'
        });
        this.enviando.set(false);
      }
    })
  }else{
    const{password2, ...developData }= this.userForm.value;

    
    this.authService.addDevelop(developData as DeveloperRequest).subscribe({
      next: (data)=> {
         this.alerta.set({
          tipo: 'exito',
          mensaje: 'Usted se ha registrado con éxito'
        });
        console.log('Usuario cargado con éxito',data);
        setTimeout(()=>
        this.router.navigate(['/login']),3000)},
      error: (err) => {
        console.error('Error al registrar el usuario', err);
        this.alerta.set({
          tipo: 'error',
          mensaje: err.error?.message || 'Ocurrió un error al registrarse.'
        });
        this.enviando.set(false);
      }
  })
}
  }

  elegirRole(role: 'client' | 'develop'):void{

    this.user.set(role)

  }

  // Metodo de validacion de fecha
  private pastDateValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) return null;
      const selectedDate = new Date(control.value);
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      return selectedDate > today ? { maxDateError: true } : null;
    };

}
}


