import { Component, input, output, ElementRef, ViewChild } from '@angular/core';
import { Genre } from '../../../../models/genre'; // Ajustá la ruta hacia tu modelo Genre
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-category-slide',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './category-slide.html',
  styleUrl: './category-slide.css',
})
export class CategorySlide {
  genres = input.required<Genre[]>();
  readonly repetitions: number[] = [1, 2, 3, 4];
}
