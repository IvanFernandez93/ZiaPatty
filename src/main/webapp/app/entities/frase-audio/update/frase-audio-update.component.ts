import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FraseAudioFormService, FraseAudioFormGroup } from './frase-audio-form.service';
import { IFraseAudio } from '../frase-audio.model';
import { FraseAudioService } from '../service/frase-audio.service';
import { IFrase } from 'app/entities/frase/frase.model';
import { FraseService } from 'app/entities/frase/service/frase.service';
import { IAudio } from 'app/entities/audio/audio.model';
import { AudioService } from 'app/entities/audio/service/audio.service';

@Component({
  selector: 'jhi-frase-audio-update',
  templateUrl: './frase-audio-update.component.html',
})
export class FraseAudioUpdateComponent implements OnInit {
  isSaving = false;
  fraseAudio: IFraseAudio | null = null;

  frasesSharedCollection: IFrase[] = [];
  audioSharedCollection: IAudio[] = [];

  editForm: FraseAudioFormGroup = this.fraseAudioFormService.createFraseAudioFormGroup();

  constructor(
    protected fraseAudioService: FraseAudioService,
    protected fraseAudioFormService: FraseAudioFormService,
    protected fraseService: FraseService,
    protected audioService: AudioService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFrase = (o1: IFrase | null, o2: IFrase | null): boolean => this.fraseService.compareFrase(o1, o2);

  compareAudio = (o1: IAudio | null, o2: IAudio | null): boolean => this.audioService.compareAudio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fraseAudio }) => {
      this.fraseAudio = fraseAudio;
      if (fraseAudio) {
        this.updateForm(fraseAudio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fraseAudio = this.fraseAudioFormService.getFraseAudio(this.editForm);
    if (fraseAudio.id !== null) {
      this.subscribeToSaveResponse(this.fraseAudioService.update(fraseAudio));
    } else {
      this.subscribeToSaveResponse(this.fraseAudioService.create(fraseAudio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFraseAudio>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fraseAudio: IFraseAudio): void {
    this.fraseAudio = fraseAudio;
    this.fraseAudioFormService.resetForm(this.editForm, fraseAudio);

    this.frasesSharedCollection = this.fraseService.addFraseToCollectionIfMissing<IFrase>(this.frasesSharedCollection, fraseAudio.frase);
    this.audioSharedCollection = this.audioService.addAudioToCollectionIfMissing<IAudio>(this.audioSharedCollection, fraseAudio.audio);
  }

  protected loadRelationshipsOptions(): void {
    this.fraseService
      .query()
      .pipe(map((res: HttpResponse<IFrase[]>) => res.body ?? []))
      .pipe(map((frases: IFrase[]) => this.fraseService.addFraseToCollectionIfMissing<IFrase>(frases, this.fraseAudio?.frase)))
      .subscribe((frases: IFrase[]) => (this.frasesSharedCollection = frases));

    this.audioService
      .query()
      .pipe(map((res: HttpResponse<IAudio[]>) => res.body ?? []))
      .pipe(map((audio: IAudio[]) => this.audioService.addAudioToCollectionIfMissing<IAudio>(audio, this.fraseAudio?.audio)))
      .subscribe((audio: IAudio[]) => (this.audioSharedCollection = audio));
  }
}
