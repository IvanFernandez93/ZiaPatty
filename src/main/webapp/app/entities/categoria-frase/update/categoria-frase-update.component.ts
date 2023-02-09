import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CategoriaFraseFormService, CategoriaFraseFormGroup } from './categoria-frase-form.service';
import { ICategoriaFrase } from '../categoria-frase.model';
import { CategoriaFraseService } from '../service/categoria-frase.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IFrase } from 'app/entities/frase/frase.model';
import { FraseService } from 'app/entities/frase/service/frase.service';

@Component({
  selector: 'jhi-categoria-frase-update',
  templateUrl: './categoria-frase-update.component.html',
})
export class CategoriaFraseUpdateComponent implements OnInit {
  isSaving = false;
  categoriaFrase: ICategoriaFrase | null = null;

  categoriasSharedCollection: ICategoria[] = [];
  frasesSharedCollection: IFrase[] = [];

  editForm: CategoriaFraseFormGroup = this.categoriaFraseFormService.createCategoriaFraseFormGroup();

  constructor(
    protected categoriaFraseService: CategoriaFraseService,
    protected categoriaFraseFormService: CategoriaFraseFormService,
    protected categoriaService: CategoriaService,
    protected fraseService: FraseService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCategoria = (o1: ICategoria | null, o2: ICategoria | null): boolean => this.categoriaService.compareCategoria(o1, o2);

  compareFrase = (o1: IFrase | null, o2: IFrase | null): boolean => this.fraseService.compareFrase(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaFrase }) => {
      this.categoriaFrase = categoriaFrase;
      if (categoriaFrase) {
        this.updateForm(categoriaFrase);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categoriaFrase = this.categoriaFraseFormService.getCategoriaFrase(this.editForm);
    if (categoriaFrase.id !== null) {
      this.subscribeToSaveResponse(this.categoriaFraseService.update(categoriaFrase));
    } else {
      this.subscribeToSaveResponse(this.categoriaFraseService.create(categoriaFrase));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoriaFrase>>): void {
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

  protected updateForm(categoriaFrase: ICategoriaFrase): void {
    this.categoriaFrase = categoriaFrase;
    this.categoriaFraseFormService.resetForm(this.editForm, categoriaFrase);

    this.categoriasSharedCollection = this.categoriaService.addCategoriaToCollectionIfMissing<ICategoria>(
      this.categoriasSharedCollection,
      categoriaFrase.categoria
    );
    this.frasesSharedCollection = this.fraseService.addFraseToCollectionIfMissing<IFrase>(
      this.frasesSharedCollection,
      categoriaFrase.frase
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaService
      .query()
      .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
      .pipe(
        map((categorias: ICategoria[]) =>
          this.categoriaService.addCategoriaToCollectionIfMissing<ICategoria>(categorias, this.categoriaFrase?.categoria)
        )
      )
      .subscribe((categorias: ICategoria[]) => (this.categoriasSharedCollection = categorias));

    this.fraseService
      .query()
      .pipe(map((res: HttpResponse<IFrase[]>) => res.body ?? []))
      .pipe(map((frases: IFrase[]) => this.fraseService.addFraseToCollectionIfMissing<IFrase>(frases, this.categoriaFrase?.frase)))
      .subscribe((frases: IFrase[]) => (this.frasesSharedCollection = frases));
  }
}
