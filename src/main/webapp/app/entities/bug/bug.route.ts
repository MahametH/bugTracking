import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBug, Bug } from 'app/shared/model/bug.model';
import { BugService } from './bug.service';
import { BugComponent } from './bug.component';
import { BugDetailComponent } from './bug-detail.component';
import { BugUpdateComponent } from './bug-update.component';

@Injectable({ providedIn: 'root' })
export class BugResolve implements Resolve<IBug> {
  constructor(private service: BugService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBug> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((bug: HttpResponse<Bug>) => {
          if (bug.body) {
            return of(bug.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bug());
  }
}

export const bugRoute: Routes = [
  {
    path: '',
    component: BugComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bugTrackingApp.bug.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BugDetailComponent,
    resolve: {
      bug: BugResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bugTrackingApp.bug.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BugUpdateComponent,
    resolve: {
      bug: BugResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bugTrackingApp.bug.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BugUpdateComponent,
    resolve: {
      bug: BugResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bugTrackingApp.bug.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
