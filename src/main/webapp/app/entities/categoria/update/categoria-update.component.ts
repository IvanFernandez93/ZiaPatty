import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CategoriaFormService, CategoriaFormGroup } from './categoria-form.service';
import { ICategoria } from '../categoria.model';
import { CategoriaService } from '../service/categoria.service';
import { Stato } from 'app/entities/enumerations/stato.model';

@Component({
  selector: 'jhi-categoria-update',
  templateUrl: './categoria-update.component.html',
})
export class CategoriaUpdateComponent implements OnInit {
  isSaving = false;
  categoria: ICategoria | null = null;
  statoValues = Object.keys(Stato);

  categoriasSharedCollection: ICategoria[] = [];

  editForm: CategoriaFormGroup = this.categoriaFormService.createCategoriaFormGroup();

  constructor(
    protected categoriaService: CategoriaService,
    protected categoriaFormService: CategoriaFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCategoria = (o1: ICategoria | null, o2: ICategoria | null): boolean => this.categoriaService.compareCategoria(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoria }) => {
      this.categoria = categoria;
      if (categoria) {
        this.updateForm(categoria);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categoria = this.categoriaFormService.getCategoria(this.editForm);
    if (categoria.idCategoria !== null) {
      this.subscribeToSaveResponse(this.categoriaService.update(categoria));
    } else {
      this.subscribeToSaveResponse(this.categoriaService.create(categoria));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoria>>): void {
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

  protected updateForm(categoria: ICategoria): void {
    this.categoria = categoria;
    this.categoriaFormService.resetForm(this.editForm, categoria);

    this.categoriasSharedCollection = this.categoriaService.addCategoriaToCollectionIfMissing<ICategoria>(
      this.categoriasSharedCollection,
      categoria.categorieFiglie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaService
      .query()
      .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
      .pipe(
        map((categorias: ICategoria[]) =>
          this.categoriaService.addCategoriaToCollectionIfMissing<ICategoria>(categorias, this.categoria?.categorieFiglie)
        )
      )
      .subscribe((categorias: ICategoria[]) => (this.categoriasSharedCollection = categorias));
  }
}
