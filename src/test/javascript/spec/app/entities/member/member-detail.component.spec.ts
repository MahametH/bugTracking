import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BugTrackingTestModule } from '../../../test.module';
import { MemberDetailComponent } from 'app/entities/member/member-detail.component';
import { Member } from 'app/shared/model/member.model';

describe('Component Tests', () => {
  describe('Member Management Detail Component', () => {
    let comp: MemberDetailComponent;
    let fixture: ComponentFixture<MemberDetailComponent>;
    const route = ({ data: of({ member: new Member(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BugTrackingTestModule],
        declarations: [MemberDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(MemberDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MemberDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load member on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.member).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
