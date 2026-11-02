import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-company-slide',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './company-slide.html',
  styleUrls: ['./company-slide.css']
})
export class CompanySlide {
  companies = input<string[]>([]);
  repetitions = [1, 2, 3, 4];
}