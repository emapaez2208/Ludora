import { Component, inject, OnInit, signal } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  imports: [ReactiveFormsModule, RouterLink],
  selector: 'app-login',
  styleUrl: './login.css',
  templateUrl: './login.html',
})
export class Login implements OnInit{

  service = inject(AuthService)
  form! : FormGroup
  fb = inject(FormBuilder)
  router = inject(Router)
  control = signal(false)
  logFalse = signal(false)
  log403 = signal(false)

  ngOnInit(): void {
    this.createForm()
  }

  createForm(){
    this.form = this.fb.group({
      username : ['', Validators.required],
      password : ['', Validators.required]
    })
  }

  onSubmit(){

    if(this.form.invalid) return

    this.logFalse.set(false)
    this.log403.set(false)
    this.control.set(true)

    this.service.login(this.form.value).subscribe({
      next: () => {
        console.log('loggeado')
        this.router.navigate(['/home'])
      },
      error: (e : HttpErrorResponse) => {
        console.error(e)
        if(e.status === 401){
          this.control.set(false)
          this.logFalse.set(true)
        }else if(e.status === 403){
          this.control.set(false)
          this.log403.set(true)
        }
      }
    })
  }
}
