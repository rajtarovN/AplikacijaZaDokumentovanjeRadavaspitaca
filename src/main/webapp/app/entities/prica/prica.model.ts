import { IPlanPrice } from 'app/entities/plan-price/plan-price.model';
import { IKonacnaPrica } from 'app/entities/konacna-prica/konacna-prica.model';
import { IKomentarNaPricu } from 'app/entities/komentar-na-pricu/komentar-na-pricu.model';
import { IDnevnik } from 'app/entities/dnevnik/dnevnik.model';

export interface IPrica {
  id?: number;
  planPrice?: IPlanPrice | null;
  konacnaPrica?: IKonacnaPrica | null;
  komentarNaPricus?: IKomentarNaPricu[] | null;
  dnevnik?: IDnevnik | null;
}

export class Prica implements IPrica {
  constructor(
    public id?: number,
    public planPrice?: IPlanPrice | null,
    public konacnaPrica?: IKonacnaPrica | null,
    public komentarNaPricus?: IKomentarNaPricu[] | null,
    public dnevnik?: IDnevnik | null
  ) {}
}

export function getPricaIdentifier(prica: IPrica): number | undefined {
  return prica.id;
}
