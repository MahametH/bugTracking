import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BugTrackingSharedModule } from 'app/shared/shared.module';
import { BugComponent } from './bug.component';
import { BugDetailComponent } from './bug-detail.component';
import { BugUpdateComponent } from './bug-update.component';
import { BugDeleteDialogComponent } from './bug-delete-dialog.component';
import { bugRoute } from './bug.route';

@NgModule({
  imports: [BugTrackingSharedModule, RouterModule.forChild(bugRoute)],
  declarations: [BugComponent, BugDetailComponent, BugUpdateComponent, BugDeleteDialogComponent],
  entryComponents: [BugDeleteDialogComponent],
})
export class BugTrackingBugModule {}
