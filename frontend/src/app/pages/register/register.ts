import { Component, effect, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user-service';
import { ClientRequest } from '../../models/client-request';
import { DeveloperRequest } from '../../models/developer-request';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-register',
  styleUrl: './register.css',
  templateUrl: './register.html',
})
export class Register  {

  userForm!: FormGroup;

  user = signal<'client' | 'develop' >('client');
  role!: string


  private fb = inject(FormBuilder)
  private router = inject(Router)
  private route = inject(ActivatedRoute)
  private userService = inject(UserService)

  constructor(){
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
      email: ['', [Validators.required , Validators.email]],
      password: ['', Validators.required ] ,
      phone: ['', [Validators.required , Validators.min(1000000) , Validators.max(99999999999)]],
      street: ['',[ Validators.required , Validators.maxLength(25)]],
      numberStreet: ['', [Validators.required , Validators.min(1) , Validators.max(99999) , Validators.pattern('^[0-9]+$')]],
      birthDate: ['', Validators.required ]
    });
  } else{
       this.userForm = this.fb.group({
      name:  ['', [Validators.required , Validators.maxLength(32), Validators.minLength(3)]],
      lastName: ['', [Validators.required , Validators.maxLength(32), Validators.minLength(3)]],
      userName: ['', [Validators.required , Validators.maxLength(20) , Validators.minLength(5)]] ,
      email: ['', [Validators.required , Validators.email]],
      password: ['', Validators.required ] ,
      company: ['' , Validators.required]
    });
  }

}

 

  onSubmit(){
    if(this.user() ==='client'){
    const clientData : ClientRequest = this.userForm.value;

    this.userService.addUser(clientData).subscribe({
      next: (data)=> {
        console.log('usuario cargado con exito',data);
        this.router.navigate(['/login']);},
      error: (err) => {
        console.error('Error al registrar el usuario', err);
      }
    })
  }else{
    const developData : DeveloperRequest = this.userForm.value;

    
    this.userService.addDevelop(developData).subscribe({
      next: (data)=> {
        console.log('usuario cargado con exito',data);
        this.router.navigate(['/login']);},
      error: (err) => {
        console.error('Error al registrar el usuario', err);
      }
  })
}
  }

  elegirRole(role: 'client' | 'develop'):void{

    this.user.set(role)

  }

}
