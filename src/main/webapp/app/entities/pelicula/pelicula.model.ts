import {IEstreno} from 'app/entities/estreno/estreno.model';
import {IReview} from 'app/entities/review/review.model';

export interface IPelicula {
  id?: number;
  titulo?: string;
  descripcion?: string;
  enCines?: boolean | null;
  estreno?: IEstreno | null;
  reviews?: IReview[] | null;
}

export class Pelicula implements IPelicula {
  constructor(
    public id?: number,
    public titulo?: string,
    public descripcion?: string,
    public enCines?: boolean | null,
    public estreno?: IEstreno | null,
    public reviews?: IReview[] | null
  ) {
    this.enCines = this.enCines ?? false;
  }
}

export function getPeliculaIdentifier(pelicula: IPelicula): number | undefined {
  return pelicula.id;
}
