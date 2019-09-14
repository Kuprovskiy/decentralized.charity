import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DecentralizedcharitySharedModule } from 'app/shared/shared.module';
import { ProjectComponent } from './project.component';
import { ProjectDetailComponent } from './project-detail.component';
import { ProjectUpdateComponent } from './project-update.component';
import { ProjectDeletePopupComponent, ProjectDeleteDialogComponent } from './project-delete-dialog.component';
import { projectRoute, projectPopupRoute } from './project.route';
import { BrowserModule } from '@angular/platform-browser';
import 'chart.js';
import { ChartsModule } from 'ng2-charts';
import { AgmCoreModule } from '@agm/core';

const ENTITY_STATES = [...projectRoute, ...projectPopupRoute];

@NgModule({
  imports: [
    ChartsModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyCfmmb--lXAN8nfpYAk7a00pb7-ZHMEu10'
    }),
    DecentralizedcharitySharedModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
  declarations: [
    ProjectComponent,
    ProjectDetailComponent,
    ProjectUpdateComponent,
    ProjectDeleteDialogComponent,
    ProjectDeletePopupComponent
  ],
  entryComponents: [ProjectComponent, ProjectUpdateComponent, ProjectDeleteDialogComponent, ProjectDeletePopupComponent]
})
export class DecentralizedcharityProjectModule {}
