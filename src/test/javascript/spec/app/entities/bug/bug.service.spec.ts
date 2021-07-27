import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { BugService } from 'app/entities/bug/bug.service';
import { IBug, Bug } from 'app/shared/model/bug.model';
import { Priority } from 'app/shared/model/enumerations/priority.model';

describe('Service Tests', () => {
  describe('Bug Service', () => {
    let injector: TestBed;
    let service: BugService;
    let httpMock: HttpTestingController;
    let elemDefault: IBug;
    let expectedResult: IBug | IBug[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(BugService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Bug(0, 'AAAAAAA', 'AAAAAAA', Priority.LOW, false, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            closedAt: currentDate.format(DATE_TIME_FORMAT),
            reopenedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Bug', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            closedAt: currentDate.format(DATE_TIME_FORMAT),
            reopenedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            closedAt: currentDate,
            reopenedAt: currentDate,
          },
          returnedFromService
        );

        service.create(new Bug()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Bug', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            priority: 'BBBBBB',
            isResolved: true,
            closedAt: currentDate.format(DATE_TIME_FORMAT),
            reopenedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            closedAt: currentDate,
            reopenedAt: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Bug', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            priority: 'BBBBBB',
            isResolved: true,
            closedAt: currentDate.format(DATE_TIME_FORMAT),
            reopenedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            closedAt: currentDate,
            reopenedAt: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Bug', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
