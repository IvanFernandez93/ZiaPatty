import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FraseComponent } from './list/frase.component';
import { FraseDetailComponent } from './detail/frase-detail.component';
import { FraseUpdateComponent } from './update/frase-update.component';
import { FraseDeleteDialogComponent } from './delete/frase-delete-dialog.component';
import { FraseRoutingModule } from './route/frase-routing.module';

@NgModule({
  imports: [SharedModule, FraseRoutingModule],
  declarations: [FraseComponent, FraseDetailComponent, FraseUpdateComponent, FraseDeleteDialogComponent],
})
export class FraseModule {}
