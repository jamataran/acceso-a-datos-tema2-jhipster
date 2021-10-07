jest.mock('@angular/router');

import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpResponse} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {of, Subject} from 'rxjs';

import {PeliculaService} from '../service/pelicula.service';
import {IPelicula, Pelicula} from '../pelicula.model';
import {IEstreno} from 'app/entities/estreno/estreno.model';
import {EstrenoService} from 'app/entities/estreno/service/estreno.service';

import {PeliculaUpdateComponent} from './pelicula-update.component';

describe('Component Tests', () => {
  describe('Pelicula Management Update Component', () => {
    let comp: PeliculaUpdateComponent;
    let fixture: ComponentFixture<PeliculaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let peliculaService: PeliculaService;
    let estrenoService: EstrenoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PeliculaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PeliculaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeliculaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      peliculaService = TestBed.inject(PeliculaService);
      estrenoService = TestBed.inject(EstrenoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call estreno query and add missing value', () => {
        const pelicula: IPelicula = { id: 456 };
        const estreno: IEstreno = { id: 548 };
        pelicula.estreno = estreno;

        const estrenoCollection: IEstreno[] = [{ id: 296 }];
        jest.spyOn(estrenoService, 'query').mockReturnValue(of(new HttpResponse({ body: estrenoCollection })));
        const expectedCollection: IEstreno[] = [estreno, ...estrenoCollection];
        jest.spyOn(estrenoService, 'addEstrenoToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        expect(estrenoService.query).toHaveBeenCalled();
        expect(estrenoService.addEstrenoToCollectionIfMissing).toHaveBeenCalledWith(estrenoCollection, estreno);
        expect(comp.estrenosCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pelicula: IPelicula = { id: 456 };
        const estreno: IEstreno = { id: 29930 };
        pelicula.estreno = estreno;

        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pelicula));
        expect(comp.estrenosCollection).toContain(estreno);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Pelicula>>();
        const pelicula = { id: 123 };
        jest.spyOn(peliculaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pelicula }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(peliculaService.update).toHaveBeenCalledWith(pelicula);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Pelicula>>();
        const pelicula = new Pelicula();
        jest.spyOn(peliculaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pelicula }));
        saveSubject.complete();

        // THEN
        expect(peliculaService.create).toHaveBeenCalledWith(pelicula);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Pelicula>>();
        const pelicula = { id: 123 };
        jest.spyOn(peliculaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(peliculaService.update).toHaveBeenCalledWith(pelicula);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEstrenoById', () => {
        it('Should return tracked Estreno primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEstrenoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
