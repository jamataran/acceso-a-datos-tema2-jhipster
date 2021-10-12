import * as dayjs from 'dayjs';

export interface IPelicula {
  id?: number;
  titulo?: string;
  fechaEstreno?: dayjs.Dayjs | null;
  descripcion?: string | null;
  enCines?: boolean | null;
}

export class Pelicula implements IPelicula {
  constructor(
    public id?: number,
    public titulo?: string,
    public fechaEstreno?: dayjs.Dayjs | null,
    public descripcion?: string | null,
    public enCines?: boolean | null
  ) {
    this.enCines = this.enCines ?? false;
  }
}

export function getPeliculaIdentifier(pelicula: IPelicula): number | undefined {
  return pelicula.id;
}
