import {IPelicula} from 'app/entities/pelicula/pelicula.model';

export interface IActor {
  id?: number;
  nombre?: string | null;
  bio?: string | null;
  peliculas?: IPelicula[] | null;
}

export class Actor implements IActor {
  constructor(public id?: number, public nombre?: string | null, public bio?: string | null, public peliculas?: IPelicula[] | null) {}
}

export function getActorIdentifier(actor: IActor): number | undefined {
  return actor.id;
}
