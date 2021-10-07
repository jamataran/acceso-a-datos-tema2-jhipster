jest.mock('@angular/router');

import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpResponse} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {of, Subject} from 'rxjs';

import {ReviewService} from '../service/review.service';
import {IReview, Review} from '../review.model';
import {IPelicula} from 'app/entities/pelicula/pelicula.model';
import {PeliculaService} from 'app/entities/pelicula/service/pelicula.service';

import {ReviewUpdateComponent} from './review-update.component';

describe('Component Tests', () => {
  describe('Review Management Update Component', () => {
    let comp: ReviewUpdateComponent;
    let fixture: ComponentFixture<ReviewUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let reviewService: ReviewService;
    let peliculaService: PeliculaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReviewUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReviewUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReviewUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      reviewService = TestBed.inject(ReviewService);
      peliculaService = TestBed.inject(PeliculaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Pelicula query and add missing value', () => {
        const review: IReview = { id: 456 };
        const pelicula: IPelicula = { id: 53192 };
        review.pelicula = pelicula;

        const peliculaCollection: IPelicula[] = [{ id: 61783 }];
        jest.spyOn(peliculaService, 'query').mockReturnValue(of(new HttpResponse({ body: peliculaCollection })));
        const additionalPeliculas = [pelicula];
        const expectedCollection: IPelicula[] = [...additionalPeliculas, ...peliculaCollection];
        jest.spyOn(peliculaService, 'addPeliculaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ review });
        comp.ngOnInit();

        expect(peliculaService.query).toHaveBeenCalled();
        expect(peliculaService.addPeliculaToCollectionIfMissing).toHaveBeenCalledWith(peliculaCollection, ...additionalPeliculas);
        expect(comp.peliculasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const review: IReview = { id: 456 };
        const pelicula: IPelicula = { id: 964 };
        review.pelicula = pelicula;

        activatedRoute.data = of({ review });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(review));
        expect(comp.peliculasSharedCollection).toContain(pelicula);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Review>>();
        const review = { id: 123 };
        jest.spyOn(reviewService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ review });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: review }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(reviewService.update).toHaveBeenCalledWith(review);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Review>>();
        const review = new Review();
        jest.spyOn(reviewService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ review });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: review }));
        saveSubject.complete();

        // THEN
        expect(reviewService.create).toHaveBeenCalledWith(review);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Review>>();
        const review = { id: 123 };
        jest.spyOn(reviewService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ review });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(reviewService.update).toHaveBeenCalledWith(review);
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
  });
});
