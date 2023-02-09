import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFraseAudio } from '../frase-audio.model';
import { FraseAudioService } from '../service/frase-audio.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './frase-audio-delete-dialog.component.html',
})
export class FraseAudioDeleteDialogComponent {
  fraseAudio?: IFraseAudio;

  constructor(protected fraseAudioService: FraseAudioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fraseAudioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
