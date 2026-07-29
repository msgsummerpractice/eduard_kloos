import { Component, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { DogService } from './dog.service';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';

@Component({
  selector: 'app-root',
  imports: [MatButtonModule, MatToolbarModule, MatIconModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  private dogService = inject(DogService);

  dogImages = signal<string[]>([]);

  loadDogs(): void {
    const request = [
      this.dogService.getRandomDogImage(),
      this.dogService.getRandomDogImage(),
      this.dogService.getRandomDogImage(),
    ];

    forkJoin(request).subscribe((results) => {
      const images = results.map((dog) => dog.message);
      this.dogImages.set(images);
    });
  }

  getDogImage() {
    this.dogService.getRandomDogImage().subscribe((response) => {
      this.dogImages.set([response.message]);
    });
  }
}
