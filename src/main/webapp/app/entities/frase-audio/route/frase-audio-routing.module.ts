import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FraseAudioComponent } from '../list/frase-audio.component';
import { FraseAudioDetailComponent } from '../detail/frase-audio-detail.component';
import { FraseAudioUpdateComponent } from '../update/frase-audio-update.component';
import { FraseAudioRoutingResolveService } from './frase-audio-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fraseAudioRoute: Routes = [
  {
    path: '',
    component: FraseAudioComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FraseAudioDetailComponent,
    resolve: {
      fraseAudio: FraseAudioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FraseAudioUpdateComponent,
    resolve: {
      fraseAudio: FraseAudioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FraseAudioUpdateComponent,
    resolve: {
      fraseAudio: FraseAudioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fraseAudioRoute)],
  exports: [RouterModule],
})
export class FraseAudioRoutingModule {}
