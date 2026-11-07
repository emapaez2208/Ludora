import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  imports: [RouterLink],
  selector: 'app-payment',
  styleUrl: './payment.css',
  templateUrl: './payment.html',
})
export class Payment implements OnInit {

  private route = inject(ActivatedRoute)
  private router = inject(Router)

  success = signal(false)
  failure = signal(false)
  pending = signal(false)
  msjError = signal('')

  countdown = signal(10)
  private intervalId? : ReturnType<typeof setInterval>
 
  constructor(){
    const type = this.route.snapshot.params['type']

    if(type === 'success'){
      this.success.set(true)
    }else if(type === 'failure'){
      this.failure.set(true)
    }else if(type === 'pending'){
      this.pending.set(true)
    }else{
      this.msjError.set('La pagina no puede cargarse con exito, reintente mas tarde..')
    }
  }

  ngOnInit(): void {
    if (this.success()) {
      this.startCountdown()
    }
  }

    startCountdown() {
    this.intervalId = setInterval(() => {
      if (this.countdown() > 1) {
        this.countdown.update(value => value - 1);
      } else {
        this.clearCountdown();
        this.goToLibrary();
      }
    }, 1000);
  }

  goToLibrary() {
    this.clearCountdown();
    this.router.navigate(['/library']);
  }

  clearCountdown() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
      this.intervalId = undefined;
    }
  }

  ngOnDestroy() {
    this.clearCountdown();
  }

}
