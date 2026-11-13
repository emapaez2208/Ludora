import { Component, inject, signal } from '@angular/core';
import { GameService } from '../../services/game-service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AgeRangeService } from '../../services/age-range-service';
import { GenreService } from '../../services/genre-service';
import { AgeRange } from '../../models/age-range';
import { Genre } from '../../models/genre';
import { GameRequest } from '../../models/game-request';
import { CloudinaryService } from '../../services/cloudinary';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-form-game',
  styleUrl: './form-game.css',
  templateUrl: './form-game.html',
})
export class FormGame {

  cloudinaryService = inject(CloudinaryService)
  gameService = inject(GameService)
  fb = inject(FormBuilder)
  form! : FormGroup
  route = inject(ActivatedRoute)
  router = inject(Router)
  gameId? : string

  today = new Date().toLocaleDateString('en-CA')
  ageRangeService = inject(AgeRangeService)
  genreService = inject(GenreService)

  ageRanges = signal<AgeRange[]>([])
  genres = signal<Genre[]>([])

  isUploading = false; //booleano para marcar cuando la foto de portada se está subiendo

  gameAgeRangeName = '' 

  constructor(){
    this.loadAgeRange()
    this.loadGenres()
    this.loadForm()
  }

  loadForm(){
    
    this.gameId = this.route.snapshot.params['id']

    this.form = this.fb.group({
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      frontPage: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0), Validators.max(99999999.99), Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
      description: ['', Validators.maxLength(200)],
      releaseDate: ['', Validators.required],
      ageRangeExternalId: ['', Validators.required],
      genreNames: this.fb.control<string[]>([], Validators.required)
    })

    if(this.gameId){
      this.gameService.getGame(this.gameId).subscribe({
        next: data => {
          this.form.patchValue(data)
          this.form.patchValue({ genreNames: data.genres })
          this.gameAgeRangeName = data.ageRange
          this.selectAgeRange()
        },
        error: e => {
          console.error('Hubo un error al obtener el juego', e)
          alert('El juego que desea editar no se cargo con exito')
        }
      })
    }
  }

  selectAgeRange(){
    const range = this.ageRanges().find(r => r.rangeName === this.gameAgeRangeName)
    if(range){
      this.form.patchValue({ 
        ageRangeExternalId: range.externalId 
      })
    }
  }

  onFileSelected(event : Event):void{
    const input = event.target as HTMLInputElement;

    if(input.files && input.files.length > 0){
      const file: File = input.files[0];
      this.isUploading = true;

      this.cloudinaryService.uploadCover(file).subscribe({
        next: (url : string) => {
          this.form.patchValue({frontPage : url});
          this.isUploading = false;
          input.value = '';
        },
        error: (err) => {
          console.error('Error al subir la portada', err);
          alert('Error al subir la imagen');
          this.isUploading = false;
        }
      })
    }
  }

  loadAgeRange(){
    this.ageRangeService.getAgeRanges().subscribe({
      next: data => {
        this.ageRanges.set([...data].sort((a, b) => a.minAge - b.minAge))
        this.selectAgeRange()
      },

      error: e => console.error('Error al cargar AgeRanges:', e)
    })
  }

  loadGenres(){
    this.genreService.getGenres().subscribe({
      next: data => this.genres.set([...data].sort((a, b) => a.name.toLowerCase().localeCompare(b.name.toLowerCase()))),
      error: e => console.error('Error al cargar los géneros.. ', e)
    })
  }

  onGenreChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;

    const control = this.form.get('genreNames')!;
    const selectedGenres: string[] = control.value ?? [];

    if (checkbox.checked) {
      control.setValue([
        ...selectedGenres,
        checkbox.value
      ]);
    } else {
      control.setValue(
        selectedGenres.filter(name => name !== checkbox.value)
      );
    }

    control.markAsTouched();
  }

  onSubmit(){
    
    if(this.form.invalid || this.isUploading) return

    if(this.gameId){

      this.gameService.editGame(this.gameId, this.form.value).subscribe({
        next: () => {
          alert('Juego actualizado con exito. Queda en revisión hasta que un administrador lo apruebe')
          this.router.navigate(['/catalog', this.gameId])
        },
        error: e => {
          console.error('Error al actualizar el juego', e)
          alert('El juego no se pudo actualizar, verifique los campos')
        }
      })

    }else{

      this.gameService.createGame(this.form.value).subscribe({
        next: (game) => {
          alert('Juego creado con exito. Queda en revisión hasta que un administrador lo apruebe')
          this.router.navigate(['/catalog', game.externalId])
        },
        error: e => {
          console.error('Error al crear el juego', e)
          alert('El juego no se puedo crear')
        }
          
      })
    }
  }
}
