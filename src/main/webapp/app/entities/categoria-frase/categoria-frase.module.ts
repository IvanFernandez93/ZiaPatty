import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CategoriaFraseComponent } from './list/categoria-frase.component';
import { CategoriaFraseDetailComponent } from './detail/categoria-frase-detail.component';
import { CategoriaFraseUpdateComponent } from './update/categoria-frase-update.component';
import { CategoriaFraseDeleteDialogComponent } from './delete/categoria-frase-delete-dialog.component';
import { CategoriaFraseRoutingModule } from './route/categoria-frase-routing.module';

@NgModule({
  imports: [SharedModule, CategoriaFraseRoutingModule],
  declarations: [
    CategoriaFraseComponent,
    CategoriaFraseDetailComponent,
    CategoriaFraseUpdateComponent,
    CategoriaFraseDeleteDialogComponent,
  ],
})
export class CategoriaFraseModule {}
