import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize} from 'rxjs/operators';

import {Estreno, IEstreno} from '../estreno.model';
import {EstrenoService} from '../service/estreno.service';

@Component({
  selector: 'jhi-estreno-update',
  templateUrl: './estreno-update.component.html',
})
export class EstrenoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    estreno: [],
    fechaEstreno: [],
  });

  constructor(protected estrenoService: EstrenoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ estreno }) => {
      this.updateForm(estreno);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const estreno = this.createFromForm();
    if (estreno.id !== undefined) {
      this.subscribeToSaveResponse(this.estrenoService.update(estreno));
    } else {
      this.subscribeToSaveResponse(this.estrenoService.create(estreno));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEstreno>>): void {
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

  protected updateForm(estreno: IEstreno): void {
    this.editForm.patchValue({
      id: estreno.id,
      estreno: estreno.estreno,
      fechaEstreno: estreno.fechaEstreno,
    });
  }

  protected createFromForm(): IEstreno {
    return {
      ...new Estreno(),
      id: this.editForm.get(['id'])!.value,
      estreno: this.editForm.get(['estreno'])!.value,
      fechaEstreno: this.editForm.get(['fechaEstreno'])!.value,
    };
  }
}
