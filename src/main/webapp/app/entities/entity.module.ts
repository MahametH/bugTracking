import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'project',
        loadChildren: () => import('./project/project.module').then(m => m.BugTrackingProjectModule),
      },
      {
        path: 'note',
        loadChildren: () => import('./note/note.module').then(m => m.BugTrackingNoteModule),
      },
      {
        path: 'bug',
        loadChildren: () => import('./bug/bug.module').then(m => m.BugTrackingBugModule),
      },
      {
        path: 'member',
        loadChildren: () => import('./member/member.module').then(m => m.BugTrackingMemberModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class BugTrackingEntityModule {}
