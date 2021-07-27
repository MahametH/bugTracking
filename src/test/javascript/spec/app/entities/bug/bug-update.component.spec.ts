import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BugTrackingTestModule } from '../../../test.module';
import { BugUpdateComponent } from 'app/entities/bug/bug-update.component';
import { BugService } from 'app/entities/bug/bug.service';
import { Bug } from 'app/shared/model/bug.model';

describe('Component Tests', () => {
  describe('Bug Management Update Component', () => {
    let comp: BugUpdateComponent;
    let fixture: ComponentFixture<BugUpdateComponent>;
    let service: BugService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BugTrackingTestModule],
        declarations: [BugUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(BugUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BugUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BugService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Bug(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Bug();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
