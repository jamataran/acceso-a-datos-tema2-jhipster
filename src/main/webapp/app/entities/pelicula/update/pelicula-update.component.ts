import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize, map} from 'rxjs/operators';

import {IPelicula, Pelicula} from '../pelicula.model';
import {PeliculaService} from '../service/pelicula.service';
import {IEstreno} from 'app/entities/estreno/estreno.model';
import {EstrenoService} from 'app/entities/estreno/service/estreno.service';

@Component({
  selector: 'jhi-pelicula-update',
  templateUrl: './pelicula-update.component.html',
})
export class PeliculaUpdateComponent implements OnInit {
  isSaving = false;

  estrenosCollection: IEstreno[] = [];

  editForm = this.fb.group({
    id: [],
    titulo: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(250)]],
    descripcion: [null, [Validators.required, Validators.minLength(10)]],
    enCines: [],
    estreno: [],
  });

  constructor(
    protected peliculaService: PeliculaService,
    protected estrenoService: EstrenoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pelicula }) => {
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

  trackEstrenoById(index: number, item: IEstreno): number {
    return item.id!;
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
      descripcion: pelicula.descripcion,
      enCines: pelicula.enCines,
      estreno: pelicula.estreno,
    });

    this.estrenosCollection = this.estrenoService.addEstrenoToCollectionIfMissing(this.estrenosCollection, pelicula.estreno);
  }

  protected loadRelationshipsOptions(): void {
    this.estrenoService
      .query({ 'peliculaId.specified': 'false' })
      .pipe(map((res: HttpResponse<IEstreno[]>) => res.body ?? []))
      .pipe(
        map((estrenos: IEstreno[]) => this.estrenoService.addEstrenoToCollectionIfMissing(estrenos, this.editForm.get('estreno')!.value))
      )
      .subscribe((estrenos: IEstreno[]) => (this.estrenosCollection = estrenos));
  }

  protected createFromForm(): IPelicula {
    return {
      ...new Pelicula(),
      id: this.editForm.get(['id'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      enCines: this.editForm.get(['enCines'])!.value,
      estreno: this.editForm.get(['estreno'])!.value,
    };
  }
}
