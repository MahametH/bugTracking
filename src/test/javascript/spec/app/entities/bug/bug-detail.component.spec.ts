import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BugTrackingTestModule } from '../../../test.module';
import { BugDetailComponent } from 'app/entities/bug/bug-detail.component';
import { Bug } from 'app/shared/model/bug.model';

describe('Component Tests', () => {
  describe('Bug Management Detail Component', () => {
    let comp: BugDetailComponent;
    let fixture: ComponentFixture<BugDetailComponent>;
    const route = ({ data: of({ bug: new Bug(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BugTrackingTestModule],
        declarations: [BugDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(BugDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BugDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bug on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bug).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
