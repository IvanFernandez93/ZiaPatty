import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategoriaFrase } from '../categoria-frase.model';
import { CategoriaFraseService } from '../service/categoria-frase.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './categoria-frase-delete-dialog.component.html',
})
export class CategoriaFraseDeleteDialogComponent {
  categoriaFrase?: ICategoriaFrase;

  constructor(protected categoriaFraseService: CategoriaFraseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categoriaFraseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
