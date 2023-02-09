import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FraseAudioComponent } from './list/frase-audio.component';
import { FraseAudioDetailComponent } from './detail/frase-audio-detail.component';
import { FraseAudioUpdateComponent } from './update/frase-audio-update.component';
import { FraseAudioDeleteDialogComponent } from './delete/frase-audio-delete-dialog.component';
import { FraseAudioRoutingModule } from './route/frase-audio-routing.module';

@NgModule({
  imports: [SharedModule, FraseAudioRoutingModule],
  declarations: [FraseAudioComponent, FraseAudioDetailComponent, FraseAudioUpdateComponent, FraseAudioDeleteDialogComponent],
})
export class FraseAudioModule {}
