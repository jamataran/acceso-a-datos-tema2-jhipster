import * as dayjs from 'dayjs';
import {IPelicula} from 'app/entities/pelicula/pelicula.model';

export interface IEstreno {
  id?: number;
  estreno?: string | null;
  fechaEstreno?: dayjs.Dayjs | null;
  pelicula?: IPelicula | null;
}

export class Estreno implements IEstreno {
  constructor(
    public id?: number,
    public estreno?: string | null,
    public fechaEstreno?: dayjs.Dayjs | null,
    public pelicula?: IPelicula | null
  ) {}
}

export function getEstrenoIdentifier(estreno: IEstreno): number | undefined {
  return estreno.id;
}
