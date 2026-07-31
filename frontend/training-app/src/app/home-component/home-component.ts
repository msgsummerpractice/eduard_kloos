import { Component, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';
import { DogService } from '../dog.service';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-home-component',
  imports: [MatButtonModule, MatToolbarModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './home-component.html',
  styleUrls: ['./home-component.css'],
})
export class HomeComponent {
  private dogService = inject(DogService);
  dogImages = signal<string[]>([]);
  protected loading = signal(false);

  loadDogs(): void {
    this.loading.set(true);
    const request = [
      this.dogService.getRandomDogImage(),
      this.dogService.getRandomDogImage(),
      this.dogService.getRandomDogImage(),
    ];

    forkJoin(request).subscribe((results) => {
      const images = results.map((dog) => dog.message);
      this.dogImages.set(images);
      this.loading.set(false);
    });
  }

  getDogImage() {
    this.dogService.getRandomDogImage().subscribe((response) => {
      this.dogImages.set([response.message]);
    });
  }
}
