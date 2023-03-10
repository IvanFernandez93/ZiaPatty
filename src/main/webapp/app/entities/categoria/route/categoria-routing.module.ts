import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategoriaComponent } from '../list/categoria.component';
import { CategoriaDetailComponent } from '../detail/categoria-detail.component';
import { CategoriaUpdateComponent } from '../update/categoria-update.component';
import { CategoriaRoutingResolveService } from './categoria-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const categoriaRoute: Routes = [
  {
    path: '',
    component: CategoriaComponent,
    data: {
      defaultSort: 'idCategoria,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':idCategoria/view',
    component: CategoriaDetailComponent,
    resolve: {
      categoria: CategoriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategoriaUpdateComponent,
    resolve: {
      categoria: CategoriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':idCategoria/edit',
    component: CategoriaUpdateComponent,
    resolve: {
      categoria: CategoriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categoriaRoute)],
  exports: [RouterModule],
})
export class CategoriaRoutingModule {}
