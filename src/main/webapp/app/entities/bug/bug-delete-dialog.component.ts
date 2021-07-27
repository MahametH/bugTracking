import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBug } from 'app/shared/model/bug.model';
import { BugService } from './bug.service';

@Component({
  templateUrl: './bug-delete-dialog.component.html',
})
export class BugDeleteDialogComponent {
  bug?: IBug;

  constructor(protected bugService: BugService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bugService.delete(id).subscribe(() => {
      this.eventManager.broadcast('bugListModification');
      this.activeModal.close();
    });
  }
}
