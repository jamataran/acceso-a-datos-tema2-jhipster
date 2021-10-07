import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize, map} from 'rxjs/operators';

import {IReview, Review} from '../review.model';
import {ReviewService} from '../service/review.service';
import {IPelicula} from 'app/entities/pelicula/pelicula.model';
import {PeliculaService} from 'app/entities/pelicula/service/pelicula.service';

@Component({
  selector: 'jhi-review-update',
  templateUrl: './review-update.component.html',
})
export class ReviewUpdateComponent implements OnInit {
  isSaving = false;

  peliculasSharedCollection: IPelicula[] = [];

  editForm = this.fb.group({
    id: [],
    puntuacion: [null, [Validators.required, Validators.min(0), Validators.max(5)]],
    descripcion: [null, [Validators.required, Validators.minLength(5)]],
    pelicula: [],
  });

  constructor(
    protected reviewService: ReviewService,
    protected peliculaService: PeliculaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ review }) => {
      this.updateForm(review);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const review = this.createFromForm();
    if (review.id !== undefined) {
      this.subscribeToSaveResponse(this.reviewService.update(review));
    } else {
      this.subscribeToSaveResponse(this.reviewService.create(review));
    }
  }

  trackPeliculaById(index: number, item: IPelicula): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReview>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(review: IReview): void {
    this.editForm.patchValue({
      id: review.id,
      puntuacion: review.puntuacion,
      descripcion: review.descripcion,
      pelicula: review.pelicula,
    });

    this.peliculasSharedCollection = this.peliculaService.addPeliculaToCollectionIfMissing(this.peliculasSharedCollection, review.pelicula);
  }

  protected loadRelationshipsOptions(): void {
    this.peliculaService
      .query()
      .pipe(map((res: HttpResponse<IPelicula[]>) => res.body ?? []))
      .pipe(
        map((peliculas: IPelicula[]) =>
          this.peliculaService.addPeliculaToCollectionIfMissing(peliculas, this.editForm.get('pelicula')!.value)
        )
      )
      .subscribe((peliculas: IPelicula[]) => (this.peliculasSharedCollection = peliculas));
  }

  protected createFromForm(): IReview {
    return {
      ...new Review(),
      id: this.editForm.get(['id'])!.value,
      puntuacion: this.editForm.get(['puntuacion'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      pelicula: this.editForm.get(['pelicula'])!.value,
    };
  }
}
