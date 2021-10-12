jest.mock('@angular/router');

import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpResponse} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {of, Subject} from 'rxjs';

import {PeliculaService} from '../service/pelicula.service';
import {IPelicula, Pelicula} from '../pelicula.model';
import {IActor} from 'app/entities/actor/actor.model';
import {ActorService} from 'app/entities/actor/service/actor.service';

import {PeliculaUpdateComponent} from './pelicula-update.component';

describe('Component Tests', () => {
  describe('Pelicula Management Update Component', () => {
    let comp: PeliculaUpdateComponent;
    let fixture: ComponentFixture<PeliculaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let peliculaService: PeliculaService;
    let actorService: ActorService;

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
      actorService = TestBed.inject(ActorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Actor query and add missing value', () => {
        const pelicula: IPelicula = { id: 456 };
        const actors: IActor[] = [{ id: 12892 }];
        pelicula.actors = actors;

        const actorCollection: IActor[] = [{ id: 4669 }];
        jest.spyOn(actorService, 'query').mockReturnValue(of(new HttpResponse({ body: actorCollection })));
        const additionalActors = [...actors];
        const expectedCollection: IActor[] = [...additionalActors, ...actorCollection];
        jest.spyOn(actorService, 'addActorToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        expect(actorService.query).toHaveBeenCalled();
        expect(actorService.addActorToCollectionIfMissing).toHaveBeenCalledWith(actorCollection, ...additionalActors);
        expect(comp.actorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pelicula: IPelicula = { id: 456 };
        const actors: IActor = { id: 82708 };
        pelicula.actors = [actors];

        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pelicula));
        expect(comp.actorsSharedCollection).toContain(actors);
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
      describe('trackActorById', () => {
        it('Should return tracked Actor primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackActorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedActor', () => {
        it('Should return option if no Actor is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedActor(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Actor for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedActor(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Actor is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedActor(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
