export interface INote {
  id?: number;
  body?: string;
  authorLogin?: string;
  authorId?: number;
  bugTitle?: string;
  bugId?: number;
}

export class Note implements INote {
  constructor(
    public id?: number,
    public body?: string,
    public authorLogin?: string,
    public authorId?: number,
    public bugTitle?: string,
    public bugId?: number
  ) {}
}
