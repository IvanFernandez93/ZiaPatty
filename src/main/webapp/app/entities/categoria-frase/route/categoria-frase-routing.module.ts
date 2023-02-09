import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategoriaFraseComponent } from '../list/categoria-frase.component';
import { CategoriaFraseDetailComponent } from '../detail/categoria-frase-detail.component';
import { CategoriaFraseUpdateComponent } from '../update/categoria-frase-update.component';
import { CategoriaFraseRoutingResolveService } from './categoria-frase-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const categoriaFraseRoute: Routes = [
  {
    path: '',
    component: CategoriaFraseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategoriaFraseDetailComponent,
    resolve: {
      categoriaFrase: CategoriaFraseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategoriaFraseUpdateComponent,
    resolve: {
      categoriaFrase: CategoriaFraseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategoriaFraseUpdateComponent,
    resolve: {
      categoriaFrase: CategoriaFraseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categoriaFraseRoute)],
  exports: [RouterModule],
})
export class CategoriaFraseRoutingModule {}
