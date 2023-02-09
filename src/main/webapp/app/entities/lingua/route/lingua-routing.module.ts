import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LinguaComponent } from '../list/lingua.component';
import { LinguaDetailComponent } from '../detail/lingua-detail.component';
import { LinguaUpdateComponent } from '../update/lingua-update.component';
import { LinguaRoutingResolveService } from './lingua-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const linguaRoute: Routes = [
  {
    path: '',
    component: LinguaComponent,
    data: {
      defaultSort: 'idLingua,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':idLingua/view',
    component: LinguaDetailComponent,
    resolve: {
      lingua: LinguaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LinguaUpdateComponent,
    resolve: {
      lingua: LinguaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':idLingua/edit',
    component: LinguaUpdateComponent,
    resolve: {
      lingua: LinguaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(linguaRoute)],
  exports: [RouterModule],
})
export class LinguaRoutingModule {}
