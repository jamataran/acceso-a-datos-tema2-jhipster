import * as dayjs from 'dayjs';

export interface IEstreno {
  id?: number;
  estreno?: string | null;
  fechaEstreno?: dayjs.Dayjs | null;
}

export class Estreno implements IEstreno {
  constructor(public id?: number, public estreno?: string | null, public fechaEstreno?: dayjs.Dayjs | null) {}
}

export function getEstrenoIdentifier(estreno: IEstreno): number | undefined {
  return estreno.id;
}
