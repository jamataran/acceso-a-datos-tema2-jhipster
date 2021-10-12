import * as dayjs from 'dayjs';
import {IEstreno} from 'app/entities/estreno/estreno.model';

export interface IPelicula {
  id?: number;
  titulo?: string;
  fechaEstreno?: dayjs.Dayjs | null;
  descripcion?: string | null;
  enCines?: boolean | null;
  estreno?: IEstreno | null;
}

export class Pelicula implements IPelicula {
  constructor(
    public id?: number,
    public titulo?: string,
    public fechaEstreno?: dayjs.Dayjs | null,
    public descripcion?: string | null,
    public enCines?: boolean | null,
    public estreno?: IEstreno | null
  ) {
    this.enCines = this.enCines ?? false;
  }
}

export function getPeliculaIdentifier(pelicula: IPelicula): number | undefined {
  return pelicula.id;
}
