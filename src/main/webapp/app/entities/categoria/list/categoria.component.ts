import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {ICategoria} from '../categoria.model';
import {CategoriaService} from '../service/categoria.service';
import {CategoriaDeleteDialogComponent} from '../delete/categoria-delete-dialog.component';
import {DataUtils} from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-categoria',
  templateUrl: './categoria.component.html',
})
export class CategoriaComponent implements OnInit {
  categorias?: ICategoria[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected categoriaService: CategoriaService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.categoriaService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<ICategoria[]>) => {
            this.isLoading = false;
            this.categorias = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.categoriaService.query().subscribe(
      (res: HttpResponse<ICategoria[]>) => {
        this.isLoading = false;
        this.categorias = res.body ?? [];
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

  trackId(index: number, item: ICategoria): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(categoria: ICategoria): void {
    const modalRef = this.modalService.open(CategoriaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.categoria = categoria;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
