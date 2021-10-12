jest.mock('@angular/router');

import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpResponse} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {of, Subject} from 'rxjs';

import {ActorService} from '../service/actor.service';
import {Actor, IActor} from '../actor.model';
import {IPelicula} from 'app/entities/pelicula/pelicula.model';
import {PeliculaService} from 'app/entities/pelicula/service/pelicula.service';

import {ActorUpdateComponent} from './actor-update.component';

describe('Component Tests', () => {
  describe('Actor Management Update Component', () => {
    let comp: ActorUpdateComponent;
    let fixture: ComponentFixture<ActorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let actorService: ActorService;
    let peliculaService: PeliculaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ActorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ActorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ActorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      actorService = TestBed.inject(ActorService);
      peliculaService = TestBed.inject(PeliculaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Pelicula query and add missing value', () => {
        const actor: IActor = { id: 456 };
        const peliculas: IPelicula[] = [{ id: 84672 }];
        actor.peliculas = peliculas;

        const peliculaCollection: IPelicula[] = [{ id: 13796 }];
        jest.spyOn(peliculaService, 'query').mockReturnValue(of(new HttpResponse({ body: peliculaCollection })));
        const additionalPeliculas = [...peliculas];
        const expectedCollection: IPelicula[] = [...additionalPeliculas, ...peliculaCollection];
        jest.spyOn(peliculaService, 'addPeliculaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ actor });
        comp.ngOnInit();

        expect(peliculaService.query).toHaveBeenCalled();
        expect(peliculaService.addPeliculaToCollectionIfMissing).toHaveBeenCalledWith(peliculaCollection, ...additionalPeliculas);
        expect(comp.peliculasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const actor: IActor = { id: 456 };
        const peliculas: IPelicula = { id: 85531 };
        actor.peliculas = [peliculas];

        activatedRoute.data = of({ actor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(actor));
        expect(comp.peliculasSharedCollection).toContain(peliculas);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Actor>>();
        const actor = { id: 123 };
        jest.spyOn(actorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ actor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: actor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(actorService.update).toHaveBeenCalledWith(actor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Actor>>();
        const actor = new Actor();
        jest.spyOn(actorService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ actor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: actor }));
        saveSubject.complete();

        // THEN
        expect(actorService.create).toHaveBeenCalledWith(actor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Actor>>();
        const actor = { id: 123 };
        jest.spyOn(actorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ actor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(actorService.update).toHaveBeenCalledWith(actor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPeliculaById', () => {
        it('Should return tracked Pelicula primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPeliculaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedPelicula', () => {
        it('Should return option if no Pelicula is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPelicula(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Pelicula for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPelicula(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Pelicula is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPelicula(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
