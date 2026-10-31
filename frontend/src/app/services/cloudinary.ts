import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

export interface CloudinaryResponse {
  secure_url: string;
  public_id: string;
}

@Injectable({
    providedIn : 'root'
})
export class CloudinaryService {
    private http = inject(HttpClient);

    private readonly cloudName = 'cbttd5ya'
    private readonly uploadPreset = 'ludoraUnsigned'
    private readonly baseUrl = `https://api.cloudinary.com/v1_1/${this.cloudName}/image/upload`;

    private sendToCloudinary(file : File, folder: 'avatars' |'cover'): Observable<string>{
        const data = new FormData()
        data.append('file', file)
        data.append('upload_preset', this.uploadPreset)
        data.append('folder', folder)

        return this.http.post<CloudinaryResponse>(this.baseUrl, data).pipe(
            map(response => response.secure_url)
        )

    }
    
    /* Pa subir foto de perfil */ 
    uploadAvatar(file : File) : Observable<string>{
        return this.sendToCloudinary(file, 'avatars')
    }

    /* Pa subir portada de juego */
    uploadCover(file : File) : Observable<string>{
        return this.sendToCloudinary(file, 'cover')
    }
}
