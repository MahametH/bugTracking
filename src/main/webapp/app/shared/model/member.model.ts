import { Moment } from 'moment';

export interface IMember {
  id?: number;
  joinedAt?: Moment;
  projectName?: string;
  projectId?: number;
}

export class Member implements IMember {
  constructor(public id?: number, public joinedAt?: Moment, public projectName?: string, public projectId?: number) {}
}
