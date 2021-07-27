import { Moment } from 'moment';
import { INote } from 'app/shared/model/note.model';
import { Priority } from 'app/shared/model/enumerations/priority.model';

export interface IBug {
  id?: number;
  title?: string;
  description?: string;
  priority?: Priority;
  isResolved?: boolean;
  closedAt?: Moment;
  reopenedAt?: Moment;
  projectName?: string;
  projectId?: number;
  notes?: INote[];
  closedByLogin?: string;
  closedById?: number;
  reopenedByLogin?: string;
  reopenedById?: number;
}

export class Bug implements IBug {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public priority?: Priority,
    public isResolved?: boolean,
    public closedAt?: Moment,
    public reopenedAt?: Moment,
    public projectName?: string,
    public projectId?: number,
    public notes?: INote[],
    public closedByLogin?: string,
    public closedById?: number,
    public reopenedByLogin?: string,
    public reopenedById?: number
  ) {
    this.isResolved = this.isResolved || false;
  }
}
