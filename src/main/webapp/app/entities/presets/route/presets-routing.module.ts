import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PresetsComponent } from '../list/presets.component';
import { PresetsDetailComponent } from '../detail/presets-detail.component';
import { PresetsUpdateComponent } from '../update/presets-update.component';
import { PresetsRoutingResolveService } from './presets-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const presetsRoute: Routes = [
  {
    path: '',
    component: PresetsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PresetsDetailComponent,
    resolve: {
      presets: PresetsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PresetsUpdateComponent,
    resolve: {
      presets: PresetsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PresetsUpdateComponent,
    resolve: {
      presets: PresetsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(presetsRoute)],
  exports: [RouterModule],
})
export class PresetsRoutingModule {}
