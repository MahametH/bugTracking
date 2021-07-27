import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMember } from 'app/shared/model/member.model';
import { MemberService } from './member.service';

@Component({
  templateUrl: './member-delete-dialog.component.html',
})
export class MemberDeleteDialogComponent {
  member?: IMember;

  constructor(protected memberService: MemberService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.memberService.delete(id).subscribe(() => {
      this.eventManager.broadcast('memberListModification');
      this.activeModal.close();
    });
  }
}
