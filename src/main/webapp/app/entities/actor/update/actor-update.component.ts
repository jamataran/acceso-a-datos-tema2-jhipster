import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize, map} from 'rxjs/operators';

import {Actor, IActor} from '../actor.model';
import {ActorService} from '../service/actor.service';
import {IPelicula} from 'app/entities/pelicula/pelicula.model';
import {PeliculaService} from 'app/entities/pelicula/service/pelicula.service';

@Component({
  selector: 'jhi-actor-update',
  templateUrl: './actor-update.component.html',
})
export class ActorUpdateComponent implements OnInit {
  isSaving = false;

  peliculasSharedCollection: IPelicula[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    bio: [],
    peliculas: [],
  });

  constructor(
    protected actorService: ActorService,
    protected peliculaService: PeliculaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ actor }) => {
      this.updateForm(actor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const actor = this.createFromForm();
    if (actor.id !== undefined) {
      this.subscribeToSaveResponse(this.actorService.update(actor));
    } else {
      this.subscribeToSaveResponse(this.actorService.create(actor));
    }
  }

  trackPeliculaById(index: number, item: IPelicula): number {
    return item.id!;
  }

  getSelectedPelicula(option: IPelicula, selectedVals?: IPelicula[]): IPelicula {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActor>>): void {
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

  protected updateForm(actor: IActor): void {
    this.editForm.patchValue({
      id: actor.id,
      nombre: actor.nombre,
      bio: actor.bio,
      peliculas: actor.peliculas,
    });

    this.peliculasSharedCollection = this.peliculaService.addPeliculaToCollectionIfMissing(
      this.peliculasSharedCollection,
      ...(actor.peliculas ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.peliculaService
      .query()
      .pipe(map((res: HttpResponse<IPelicula[]>) => res.body ?? []))
      .pipe(
        map((peliculas: IPelicula[]) =>
          this.peliculaService.addPeliculaToCollectionIfMissing(peliculas, ...(this.editForm.get('peliculas')!.value ?? []))
        )
      )
      .subscribe((peliculas: IPelicula[]) => (this.peliculasSharedCollection = peliculas));
  }

  protected createFromForm(): IActor {
    return {
      ...new Actor(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      bio: this.editForm.get(['bio'])!.value,
      peliculas: this.editForm.get(['peliculas'])!.value,
    };
  }
}
