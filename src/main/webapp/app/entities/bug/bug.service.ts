import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBug } from 'app/shared/model/bug.model';

type EntityResponseType = HttpResponse<IBug>;
type EntityArrayResponseType = HttpResponse<IBug[]>;

@Injectable({ providedIn: 'root' })
export class BugService {
  public resourceUrl = SERVER_API_URL + 'api/bugs';

  constructor(protected http: HttpClient) {}

  create(bug: IBug): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bug);
    return this.http
      .post<IBug>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bug: IBug): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bug);
    return this.http
      .put<IBug>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBug>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBug[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(bug: IBug): IBug {
    const copy: IBug = Object.assign({}, bug, {
      closedAt: bug.closedAt && bug.closedAt.isValid() ? bug.closedAt.toJSON() : undefined,
      reopenedAt: bug.reopenedAt && bug.reopenedAt.isValid() ? bug.reopenedAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.closedAt = res.body.closedAt ? moment(res.body.closedAt) : undefined;
      res.body.reopenedAt = res.body.reopenedAt ? moment(res.body.reopenedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((bug: IBug) => {
        bug.closedAt = bug.closedAt ? moment(bug.closedAt) : undefined;
        bug.reopenedAt = bug.reopenedAt ? moment(bug.reopenedAt) : undefined;
      });
    }
    return res;
  }
}
