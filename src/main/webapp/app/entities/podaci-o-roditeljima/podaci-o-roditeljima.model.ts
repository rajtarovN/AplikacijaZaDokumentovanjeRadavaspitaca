import { IFormular } from 'app/entities/formular/formular.model';
import { RadniStatus } from 'app/entities/enumerations/radni-status.model';

export interface IPodaciORoditeljima {
  id?: number;
  jmbg?: string | null;
  ime?: string | null;
  prezime?: string | null;
  telefon?: string | null;
  firma?: string | null;
  radnoVreme?: string | null;
  radniStatus?: RadniStatus | null;
  formular?: IFormular | null;
}

export class PodaciORoditeljima implements IPodaciORoditeljima {
  constructor(
    public id?: number,
    public jmbg?: string | null,
    public ime?: string | null,
    public prezime?: string | null,
    public telefon?: string | null,
    public firma?: string | null,
    public radnoVreme?: string | null,
    public radniStatus?: RadniStatus | null,
    public formular?: IFormular | null
  ) {}
}

export function getPodaciORoditeljimaIdentifier(podaciORoditeljima: IPodaciORoditeljima): number | undefined {
  return podaciORoditeljima.id;
}
