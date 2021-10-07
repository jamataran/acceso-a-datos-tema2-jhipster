jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EstrenoService } from '../service/estreno.service';
import { IEstreno, Estreno } from '../estreno.model';

import { EstrenoUpdateComponent } from './estreno-update.component';

describe('Component Tests', () => {
  describe('Estreno Management Update Component', () => {
    let comp: EstrenoUpdateComponent;
    let fixture: ComponentFixture<EstrenoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let estrenoService: EstrenoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EstrenoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EstrenoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EstrenoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      estrenoService = TestBed.inject(EstrenoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const estreno: IEstreno = { id: 456 };

        activatedRoute.data = of({ estreno });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(estreno));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Estreno>>();
        const estreno = { id: 123 };
        jest.spyOn(estrenoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ estreno });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: estreno }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(estrenoService.update).toHaveBeenCalledWith(estreno);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Estreno>>();
        const estreno = new Estreno();
        jest.spyOn(estrenoService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ estreno });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: estreno }));
        saveSubject.complete();

        // THEN
        expect(estrenoService.create).toHaveBeenCalledWith(estreno);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Estreno>>();
        const estreno = { id: 123 };
        jest.spyOn(estrenoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ estreno });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(estrenoService.update).toHaveBeenCalledWith(estreno);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
