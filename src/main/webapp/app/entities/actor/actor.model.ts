import {IPelicula} from 'app/entities/pelicula/pelicula.model';

export interface IActor {
  id?: number;
  nombre?: string;
  apellidos?: string;
  peliculas?: IPelicula[] | null;
}

export class Actor implements IActor {
  constructor(public id?: number, public nombre?: string, public apellidos?: string, public peliculas?: IPelicula[] | null) {}
}

export function getActorIdentifier(actor: IActor): number | undefined {
  return actor.id;
}
