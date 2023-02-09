import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILingua } from '../lingua.model';

@Component({
  selector: 'jhi-lingua-detail',
  templateUrl: './lingua-detail.component.html',
})
export class LinguaDetailComponent implements OnInit {
  lingua: ILingua | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lingua }) => {
      this.lingua = lingua;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
