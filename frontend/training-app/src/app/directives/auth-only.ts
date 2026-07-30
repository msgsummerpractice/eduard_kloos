import { Directive, effect, inject, TemplateRef, ViewContainerRef } from '@angular/core';
import { Auth } from '../services/auth';

@Directive({
  selector: '[appAuthOnly]',
})
export class AuthOnly {
  private templateRef = inject(TemplateRef<unknown>);
  private viewContainer = inject(ViewContainerRef);
  private authService = inject(Auth);

  constructor() {
    effect(() => {
      this.viewContainer.clear();

      if (this.authService.isAuthenticated()) {
        this.viewContainer.createEmbeddedView(this.templateRef);
      }
    });
  }
}
