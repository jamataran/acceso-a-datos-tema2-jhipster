import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize, map} from 'rxjs/operators';

import * as dayjs from 'dayjs';
import {DATE_TIME_FORMAT} from 'app/config/input.constants';

import {IPelicula, Pelicula} from '../pelicula.model';
import {PeliculaService} from '../service/pelicula.service';
import {IActor} from 'app/entities/actor/actor.model';
import {ActorService} from 'app/entities/actor/service/actor.service';

@Component({
  selector: 'jhi-pelicula-update',
  templateUrl: './pelicula-update.component.html',
})
export class PeliculaUpdateComponent implements OnInit {
  isSaving = false;

  actorsSharedCollection: IActor[] = [];

  editForm = this.fb.group({
    id: [],
    titulo: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(255)]],
    fechaEstreno: [],
    descripcion: [],
    enCines: [],
    actors: [],
  });

  constructor(
    protected peliculaService: PeliculaService,
    protected actorService: ActorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pelicula }) => {
      if (pelicula.id === undefined) {
        const today = dayjs().startOf('day');
        pelicula.fechaEstreno = today;
      }

      this.updateForm(pelicula);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pelicula = this.createFromForm();
    if (pelicula.id !== undefined) {
      this.subscribeToSaveResponse(this.peliculaService.update(pelicula));
    } else {
      this.subscribeToSaveResponse(this.peliculaService.create(pelicula));
    }
  }

  trackActorById(index: number, item: IActor): number {
    return item.id!;
  }

  getSelectedActor(option: IActor, selectedVals?: IActor[]): IActor {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPelicula>>): void {
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

  protected updateForm(pelicula: IPelicula): void {
    this.editForm.patchValue({
      id: pelicula.id,
      titulo: pelicula.titulo,
      fechaEstreno: pelicula.fechaEstreno ? pelicula.fechaEstreno.format(DATE_TIME_FORMAT) : null,
      descripcion: pelicula.descripcion,
      enCines: pelicula.enCines,
      actors: pelicula.actors,
    });

    this.actorsSharedCollection = this.actorService.addActorToCollectionIfMissing(this.actorsSharedCollection, ...(pelicula.actors ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.actorService
      .query()
      .pipe(map((res: HttpResponse<IActor[]>) => res.body ?? []))
      .pipe(
        map((actors: IActor[]) => this.actorService.addActorToCollectionIfMissing(actors, ...(this.editForm.get('actors')!.value ?? [])))
      )
      .subscribe((actors: IActor[]) => (this.actorsSharedCollection = actors));
  }

  protected createFromForm(): IPelicula {
    return {
      ...new Pelicula(),
      id: this.editForm.get(['id'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      fechaEstreno: this.editForm.get(['fechaEstreno'])!.value
        ? dayjs(this.editForm.get(['fechaEstreno'])!.value, DATE_TIME_FORMAT)
        : undefined,
      descripcion: this.editForm.get(['descripcion'])!.value,
      enCines: this.editForm.get(['enCines'])!.value,
      actors: this.editForm.get(['actors'])!.value,
    };
  }
}
