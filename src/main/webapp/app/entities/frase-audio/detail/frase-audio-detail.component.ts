import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFraseAudio } from '../frase-audio.model';

@Component({
  selector: 'jhi-frase-audio-detail',
  templateUrl: './frase-audio-detail.component.html',
})
export class FraseAudioDetailComponent implements OnInit {
  fraseAudio: IFraseAudio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fraseAudio }) => {
      this.fraseAudio = fraseAudio;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
