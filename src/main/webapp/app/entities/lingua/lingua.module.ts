import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LinguaComponent } from './list/lingua.component';
import { LinguaDetailComponent } from './detail/lingua-detail.component';
import { LinguaUpdateComponent } from './update/lingua-update.component';
import { LinguaDeleteDialogComponent } from './delete/lingua-delete-dialog.component';
import { LinguaRoutingModule } from './route/lingua-routing.module';

@NgModule({
  imports: [SharedModule, LinguaRoutingModule],
  declarations: [LinguaComponent, LinguaDetailComponent, LinguaUpdateComponent, LinguaDeleteDialogComponent],
})
export class LinguaModule {}
