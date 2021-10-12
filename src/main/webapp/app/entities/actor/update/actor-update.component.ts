import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize} from 'rxjs/operators';

import {Actor, IActor} from '../actor.model';
import {ActorService} from '../service/actor.service';

@Component({
  selector: 'jhi-actor-update',
  templateUrl: './actor-update.component.html',
})
export class ActorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(255)]],
    apellidos: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(255)]],
  });

  constructor(protected actorService: ActorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ actor }) => {
      this.updateForm(actor);
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
      apellidos: actor.apellidos,
    });
  }

  protected createFromForm(): IActor {
    return {
      ...new Actor(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
    };
  }
}
