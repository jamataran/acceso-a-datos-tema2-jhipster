jest.mock('@angular/router');

import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpResponse} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {of, Subject} from 'rxjs';

import {ActorService} from '../service/actor.service';
import {Actor, IActor} from '../actor.model';

import {ActorUpdateComponent} from './actor-update.component';

describe('Component Tests', () => {
  describe('Actor Management Update Component', () => {
    let comp: ActorUpdateComponent;
    let fixture: ComponentFixture<ActorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let actorService: ActorService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const actor: IActor = { id: 456 };

        activatedRoute.data = of({ actor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(actor));
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
  });
});
