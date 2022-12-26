import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PresetsComponent } from './list/presets.component';
import { PresetsDetailComponent } from './detail/presets-detail.component';
import { PresetsUpdateComponent } from './update/presets-update.component';
import { PresetsDeleteDialogComponent } from './delete/presets-delete-dialog.component';
import { PresetsRoutingModule } from './route/presets-routing.module';

@NgModule({
  imports: [SharedModule, PresetsRoutingModule],
  declarations: [PresetsComponent, PresetsDetailComponent, PresetsUpdateComponent, PresetsDeleteDialogComponent],
})
export class PresetsModule {}
