import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'username',
})
export class UsernamePipe implements PipeTransform {
  transform(value: string): string {
    return value.replace('.', ' ').replace(/\b\w/g, (char) => char.toUpperCase());
  }
}
