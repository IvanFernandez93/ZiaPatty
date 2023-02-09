import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategoriaFrase } from '../categoria-frase.model';

@Component({
  selector: 'jhi-categoria-frase-detail',
  templateUrl: './categoria-frase-detail.component.html',
})
export class CategoriaFraseDetailComponent implements OnInit {
  categoriaFrase: ICategoriaFrase | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaFrase }) => {
      this.categoriaFrase = categoriaFrase;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
