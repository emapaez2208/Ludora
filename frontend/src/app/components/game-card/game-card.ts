import { Component, input } from '@angular/core';
import { Game } from '../../models/game';
import { RouterLink } from '@angular/router';

@Component({
  imports: [RouterLink],
  selector: 'app-game-card',
  styleUrl: './game-card.css',
  templateUrl: './game-card.html',
})
export class GameCard {
  game = input.required<Game>();
  showStatus = input(false);
  developerView = input(false);
}
