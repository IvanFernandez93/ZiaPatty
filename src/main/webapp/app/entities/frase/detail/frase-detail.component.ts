import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFrase } from '../frase.model';

@Component({
  selector: 'jhi-frase-detail',
  templateUrl: './frase-detail.component.html',
})
export class FraseDetailComponent implements OnInit {
  frase: IFrase | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frase }) => {
      this.frase = frase;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
