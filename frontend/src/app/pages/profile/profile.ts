import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UserStateService } from '../../services/user-state-service';

@Component({
  imports: [RouterLink],
  selector: 'app-profile',
  styleUrl: './profile.css',
  templateUrl: './profile.html',
})
export class Profile{

  userStateService = inject(UserStateService)

}
