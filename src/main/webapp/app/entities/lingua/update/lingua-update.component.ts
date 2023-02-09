import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { LinguaFormService, LinguaFormGroup } from './lingua-form.service';
import { ILingua } from '../lingua.model';
import { LinguaService } from '../service/lingua.service';

@Component({
  selector: 'jhi-lingua-update',
  templateUrl: './lingua-update.component.html',
})
export class LinguaUpdateComponent implements OnInit {
  isSaving = false;
  lingua: ILingua | null = null;

  editForm: LinguaFormGroup = this.linguaFormService.createLinguaFormGroup();

  constructor(
    protected linguaService: LinguaService,
    protected linguaFormService: LinguaFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lingua }) => {
      this.lingua = lingua;
      if (lingua) {
        this.updateForm(lingua);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lingua = this.linguaFormService.getLingua(this.editForm);
    if (lingua.idLingua !== null) {
      this.subscribeToSaveResponse(this.linguaService.update(lingua));
    } else {
      this.subscribeToSaveResponse(this.linguaService.create(lingua));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILingua>>): void {
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

  protected updateForm(lingua: ILingua): void {
    this.lingua = lingua;
    this.linguaFormService.resetForm(this.editForm, lingua);
  }
}
