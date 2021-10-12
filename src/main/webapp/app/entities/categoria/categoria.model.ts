export interface ICategoria {
  id?: number;
  nombre?: string;
  imagenContentType?: string;
  imagen?: string;
}

export class Categoria implements ICategoria {
  constructor(public id?: number, public nombre?: string, public imagenContentType?: string, public imagen?: string) {
  }
}

export function getCategoriaIdentifier(categoria: ICategoria): number | undefined {
  return categoria.id;
}
