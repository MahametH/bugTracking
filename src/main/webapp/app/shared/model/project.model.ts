import { IMember } from 'app/shared/model/member.model';
import { IBug } from 'app/shared/model/bug.model';

export interface IProject {
  id?: number;
  name?: string;
  members?: IMember[];
  bugs?: IBug[];
}

export class Project implements IProject {
  constructor(public id?: number, public name?: string, public members?: IMember[], public bugs?: IBug[]) {}
}
