import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AudioFormService, AudioFormGroup } from './audio-form.service';
import { IAudio } from '../audio.model';
import { AudioService } from '../service/audio.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Stato } from 'app/entities/enumerations/stato.model';

@Component({
  selector: 'jhi-audio-update',
  templateUrl: './audio-update.component.html',
})
export class AudioUpdateComponent implements OnInit {
  isSaving = false;
  audio: IAudio | null = null;
  statoValues = Object.keys(Stato);

  editForm: AudioFormGroup = this.audioFormService.createAudioFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected audioService: AudioService,
    protected audioFormService: AudioFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ audio }) => {
      this.audio = audio;
      if (audio) {
        this.updateForm(audio);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('ziaPattyApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const audio = this.audioFormService.getAudio(this.editForm);
    if (audio.idAudio !== null) {
      this.subscribeToSaveResponse(this.audioService.update(audio));
    } else {
      this.subscribeToSaveResponse(this.audioService.create(audio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAudio>>): void {
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

  protected updateForm(audio: IAudio): void {
    this.audio = audio;
    this.audioFormService.resetForm(this.editForm, audio);
  }
}
