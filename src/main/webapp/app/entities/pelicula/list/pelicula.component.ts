import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {IPelicula} from '../pelicula.model';
import {PeliculaService} from '../service/pelicula.service';
import {PeliculaDeleteDialogComponent} from '../delete/pelicula-delete-dialog.component';

@Component({
  selector: 'jhi-pelicula',
  templateUrl: './pelicula.component.html',
})
export class PeliculaComponent implements OnInit {
  peliculas?: IPelicula[];
  isLoading = false;
  currentSearch: string;

  constructor(protected peliculaService: PeliculaService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.peliculaService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IPelicula[]>) => {
            this.isLoading = false;
            this.peliculas = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.peliculaService.query().subscribe(
      (res: HttpResponse<IPelicula[]>) => {
        this.isLoading = false;
        this.peliculas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPelicula): number {
    return item.id!;
  }

  delete(pelicula: IPelicula): void {
    const modalRef = this.modalService.open(PeliculaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pelicula = pelicula;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
