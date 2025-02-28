import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBug } from 'app/shared/model/bug.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { BugService } from './bug.service';
import { BugDeleteDialogComponent } from './bug-delete-dialog.component';

@Component({
  selector: 'jhi-bug',
  templateUrl: './bug.component.html',
})
export class BugComponent implements OnInit, OnDestroy {
  bugs: IBug[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected bugService: BugService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.bugs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.bugService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IBug[]>) => this.paginateBugs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.bugs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBugs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBug): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBugs(): void {
    this.eventSubscriber = this.eventManager.subscribe('bugListModification', () => this.reset());
  }

  delete(bug: IBug): void {
    const modalRef = this.modalService.open(BugDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bug = bug;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateBugs(data: IBug[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.bugs.push(data[i]);
      }
    }
  }
}
