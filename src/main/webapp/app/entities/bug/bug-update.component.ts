import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IBug, Bug } from 'app/shared/model/bug.model';
import { BugService } from './bug.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project/project.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = IProject | IUser;

@Component({
  selector: 'jhi-bug-update',
  templateUrl: './bug-update.component.html',
})
export class BugUpdateComponent implements OnInit {
  isSaving = false;
  projects: IProject[] = [];
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    description: [],
    priority: [],
    isResolved: [],
    closedAt: [],
    reopenedAt: [],
    projectId: [null, Validators.required],
    closedById: [],
    reopenedById: [],
  });

  constructor(
    protected bugService: BugService,
    protected projectService: ProjectService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bug }) => {
      if (!bug.id) {
        const today = moment().startOf('day');
        bug.closedAt = today;
        bug.reopenedAt = today;
      }

      this.updateForm(bug);

      this.projectService.query().subscribe((res: HttpResponse<IProject[]>) => (this.projects = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(bug: IBug): void {
    this.editForm.patchValue({
      id: bug.id,
      title: bug.title,
      description: bug.description,
      priority: bug.priority,
      isResolved: bug.isResolved,
      closedAt: bug.closedAt ? bug.closedAt.format(DATE_TIME_FORMAT) : null,
      reopenedAt: bug.reopenedAt ? bug.reopenedAt.format(DATE_TIME_FORMAT) : null,
      projectId: bug.projectId,
      closedById: bug.closedById,
      reopenedById: bug.reopenedById,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bug = this.createFromForm();
    if (bug.id !== undefined) {
      this.subscribeToSaveResponse(this.bugService.update(bug));
    } else {
      this.subscribeToSaveResponse(this.bugService.create(bug));
    }
  }

  private createFromForm(): IBug {
    return {
      ...new Bug(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      priority: this.editForm.get(['priority'])!.value,
      isResolved: this.editForm.get(['isResolved'])!.value,
      closedAt: this.editForm.get(['closedAt'])!.value ? moment(this.editForm.get(['closedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      reopenedAt: this.editForm.get(['reopenedAt'])!.value ? moment(this.editForm.get(['reopenedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      projectId: this.editForm.get(['projectId'])!.value,
      closedById: this.editForm.get(['closedById'])!.value,
      reopenedById: this.editForm.get(['reopenedById'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBug>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
