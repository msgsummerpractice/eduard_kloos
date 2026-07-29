import { Component, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { DogService } from './dog.service';

@Component({
  selector: 'app-root',
  imports: [MatButtonModule, MatToolbarModule, MatIconModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  private dogService = inject(DogService);

  dogImage = signal<string>('');

  getDogImage() {
    this.dogService.getRandomDogImage().subscribe((response) => {
      this.dogImage.set(response.message);
    });
  }
}
