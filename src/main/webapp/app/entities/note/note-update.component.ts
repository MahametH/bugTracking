import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { INote, Note } from 'app/shared/model/note.model';
import { NoteService } from './note.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IBug } from 'app/shared/model/bug.model';
import { BugService } from 'app/entities/bug/bug.service';

type SelectableEntity = IUser | IBug;

@Component({
  selector: 'jhi-note-update',
  templateUrl: './note-update.component.html',
})
export class NoteUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  bugs: IBug[] = [];

  editForm = this.fb.group({
    id: [],
    body: [],
    authorId: [null, Validators.required],
    bugId: [null, Validators.required],
  });

  constructor(
    protected noteService: NoteService,
    protected userService: UserService,
    protected bugService: BugService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ note }) => {
      this.updateForm(note);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.bugService.query().subscribe((res: HttpResponse<IBug[]>) => (this.bugs = res.body || []));
    });
  }

  updateForm(note: INote): void {
    this.editForm.patchValue({
      id: note.id,
      body: note.body,
      authorId: note.authorId,
      bugId: note.bugId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const note = this.createFromForm();
    if (note.id !== undefined) {
      this.subscribeToSaveResponse(this.noteService.update(note));
    } else {
      this.subscribeToSaveResponse(this.noteService.create(note));
    }
  }

  private createFromForm(): INote {
    return {
      ...new Note(),
      id: this.editForm.get(['id'])!.value,
      body: this.editForm.get(['body'])!.value,
      authorId: this.editForm.get(['authorId'])!.value,
      bugId: this.editForm.get(['bugId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INote>>): void {
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
