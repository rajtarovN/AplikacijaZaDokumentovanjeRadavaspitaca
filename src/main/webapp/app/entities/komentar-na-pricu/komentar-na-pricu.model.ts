import dayjs from 'dayjs/esm';
import { IPrica } from 'app/entities/prica/prica.model';

export interface IKomentarNaPricu {
  id?: number;
  tekst?: string | null;
  datum?: dayjs.Dayjs | null;
  prica?: IPrica | null;
}

export class KomentarNaPricu implements IKomentarNaPricu {
  constructor(public id?: number, public tekst?: string | null, public datum?: dayjs.Dayjs | null, public prica?: IPrica | null) {}
}

export function getKomentarNaPricuIdentifier(komentarNaPricu: IKomentarNaPricu): number | undefined {
  return komentarNaPricu.id;
}
