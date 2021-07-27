import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IMember, Member } from 'app/shared/model/member.model';
import { MemberService } from './member.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project/project.service';

@Component({
  selector: 'jhi-member-update',
  templateUrl: './member-update.component.html',
})
export class MemberUpdateComponent implements OnInit {
  isSaving = false;
  projects: IProject[] = [];

  editForm = this.fb.group({
    id: [],
    joinedAt: [],
    projectId: [null, Validators.required],
  });

  constructor(
    protected memberService: MemberService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ member }) => {
      if (!member.id) {
        const today = moment().startOf('day');
        member.joinedAt = today;
      }

      this.updateForm(member);

      this.projectService.query().subscribe((res: HttpResponse<IProject[]>) => (this.projects = res.body || []));
    });
  }

  updateForm(member: IMember): void {
    this.editForm.patchValue({
      id: member.id,
      joinedAt: member.joinedAt ? member.joinedAt.format(DATE_TIME_FORMAT) : null,
      projectId: member.projectId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const member = this.createFromForm();
    if (member.id !== undefined) {
      this.subscribeToSaveResponse(this.memberService.update(member));
    } else {
      this.subscribeToSaveResponse(this.memberService.create(member));
    }
  }

  private createFromForm(): IMember {
    return {
      ...new Member(),
      id: this.editForm.get(['id'])!.value,
      joinedAt: this.editForm.get(['joinedAt'])!.value ? moment(this.editForm.get(['joinedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      projectId: this.editForm.get(['projectId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMember>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IProject): any {
    return item.id;
  }
}
