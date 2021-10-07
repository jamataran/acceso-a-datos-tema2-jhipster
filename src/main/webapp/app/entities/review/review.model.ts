import {IPelicula} from 'app/entities/pelicula/pelicula.model';

export interface IReview {
  id?: number;
  puntuacion?: number;
  descripcion?: string;
  pelicula?: IPelicula | null;
}

export class Review implements IReview {
  constructor(public id?: number, public puntuacion?: number, public descripcion?: string, public pelicula?: IPelicula | null) {}
}

export function getReviewIdentifier(review: IReview): number | undefined {
  return review.id;
}
