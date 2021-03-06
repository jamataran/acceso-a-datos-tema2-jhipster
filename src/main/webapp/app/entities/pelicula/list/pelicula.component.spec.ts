jest.mock('@angular/router');

import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpHeaders, HttpResponse} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ActivatedRoute} from '@angular/router';
import {of} from 'rxjs';

import {PeliculaService} from '../service/pelicula.service';

import {PeliculaComponent} from './pelicula.component';

describe('Component Tests', () => {
  describe('Pelicula Management Component', () => {
    let comp: PeliculaComponent;
    let fixture: ComponentFixture<PeliculaComponent>;
    let service: PeliculaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PeliculaComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(PeliculaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeliculaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PeliculaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.peliculas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
