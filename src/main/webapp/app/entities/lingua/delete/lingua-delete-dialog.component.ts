import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILingua } from '../lingua.model';
import { LinguaService } from '../service/lingua.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './lingua-delete-dialog.component.html',
})
export class LinguaDeleteDialogComponent {
  lingua?: ILingua;

  constructor(protected linguaService: LinguaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.linguaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
