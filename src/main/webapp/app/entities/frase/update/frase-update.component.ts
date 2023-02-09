import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FraseFormService, FraseFormGroup } from './frase-form.service';
import { IFrase } from '../frase.model';
import { FraseService } from '../service/frase.service';
import { ILingua } from 'app/entities/lingua/lingua.model';
import { LinguaService } from 'app/entities/lingua/service/lingua.service';
import { Stato } from 'app/entities/enumerations/stato.model';

@Component({
  selector: 'jhi-frase-update',
  templateUrl: './frase-update.component.html',
})
export class FraseUpdateComponent implements OnInit {
  isSaving = false;
  frase: IFrase | null = null;
  statoValues = Object.keys(Stato);

  linguasSharedCollection: ILingua[] = [];

  editForm: FraseFormGroup = this.fraseFormService.createFraseFormGroup();

  constructor(
    protected fraseService: FraseService,
    protected fraseFormService: FraseFormService,
    protected linguaService: LinguaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareLingua = (o1: ILingua | null, o2: ILingua | null): boolean => this.linguaService.compareLingua(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frase }) => {
      this.frase = frase;
      if (frase) {
        this.updateForm(frase);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const frase = this.fraseFormService.getFrase(this.editForm);
    if (frase.id !== null) {
      this.subscribeToSaveResponse(this.fraseService.update(frase));
    } else {
      this.subscribeToSaveResponse(this.fraseService.create(frase));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFrase>>): void {
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

  protected updateForm(frase: IFrase): void {
    this.frase = frase;
    this.fraseFormService.resetForm(this.editForm, frase);

    this.linguasSharedCollection = this.linguaService.addLinguaToCollectionIfMissing<ILingua>(this.linguasSharedCollection, frase.lingua);
  }

  protected loadRelationshipsOptions(): void {
    this.linguaService
      .query()
      .pipe(map((res: HttpResponse<ILingua[]>) => res.body ?? []))
      .pipe(map((linguas: ILingua[]) => this.linguaService.addLinguaToCollectionIfMissing<ILingua>(linguas, this.frase?.lingua)))
      .subscribe((linguas: ILingua[]) => (this.linguasSharedCollection = linguas));
  }
}
