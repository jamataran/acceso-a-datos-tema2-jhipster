import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import * as dayjs from 'dayjs';

import {isPresent} from 'app/core/util/operators';
import {DATE_FORMAT} from 'app/config/input.constants';
import {ApplicationConfigService} from 'app/core/config/application-config.service';
import {createRequestOption} from 'app/core/request/request-util';
import {SearchWithPagination} from 'app/core/request/request.model';
import {getEstrenoIdentifier, IEstreno} from '../estreno.model';

export type EntityResponseType = HttpResponse<IEstreno>;
export type EntityArrayResponseType = HttpResponse<IEstreno[]>;

@Injectable({ providedIn: 'root' })
export class EstrenoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/estrenos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/estrenos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(estreno: IEstreno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(estreno);
    return this.http
      .post<IEstreno>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(estreno: IEstreno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(estreno);
    return this.http
      .put<IEstreno>(`${this.resourceUrl}/${getEstrenoIdentifier(estreno) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(estreno: IEstreno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(estreno);
    return this.http
      .patch<IEstreno>(`${this.resourceUrl}/${getEstrenoIdentifier(estreno) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEstreno>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEstreno[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEstreno[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addEstrenoToCollectionIfMissing(estrenoCollection: IEstreno[], ...estrenosToCheck: (IEstreno | null | undefined)[]): IEstreno[] {
    const estrenos: IEstreno[] = estrenosToCheck.filter(isPresent);
    if (estrenos.length > 0) {
      const estrenoCollectionIdentifiers = estrenoCollection.map(estrenoItem => getEstrenoIdentifier(estrenoItem)!);
      const estrenosToAdd = estrenos.filter(estrenoItem => {
        const estrenoIdentifier = getEstrenoIdentifier(estrenoItem);
        if (estrenoIdentifier == null || estrenoCollectionIdentifiers.includes(estrenoIdentifier)) {
          return false;
        }
        estrenoCollectionIdentifiers.push(estrenoIdentifier);
        return true;
      });
      return [...estrenosToAdd, ...estrenoCollection];
    }
    return estrenoCollection;
  }

  protected convertDateFromClient(estreno: IEstreno): IEstreno {
    return Object.assign({}, estreno, {
      fechaEstreno: estreno.fechaEstreno?.isValid() ? estreno.fechaEstreno.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaEstreno = res.body.fechaEstreno ? dayjs(res.body.fechaEstreno) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((estreno: IEstreno) => {
        estreno.fechaEstreno = estreno.fechaEstreno ? dayjs(estreno.fechaEstreno) : undefined;
      });
    }
    return res;
  }
}
