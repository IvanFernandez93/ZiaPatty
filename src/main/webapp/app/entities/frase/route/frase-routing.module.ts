import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FraseComponent } from '../list/frase.component';
import { FraseDetailComponent } from '../detail/frase-detail.component';
import { FraseUpdateComponent } from '../update/frase-update.component';
import { FraseRoutingResolveService } from './frase-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fraseRoute: Routes = [
  {
    path: '',
    component: FraseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FraseDetailComponent,
    resolve: {
      frase: FraseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FraseUpdateComponent,
    resolve: {
      frase: FraseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FraseUpdateComponent,
    resolve: {
      frase: FraseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fraseRoute)],
  exports: [RouterModule],
})
export class FraseRoutingModule {}
