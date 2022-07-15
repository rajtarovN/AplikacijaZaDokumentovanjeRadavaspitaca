import { IDete } from 'app/entities/dete/dete.model';
import { IDnevnik } from 'app/entities/dnevnik/dnevnik.model';
import { TipGrupe } from 'app/entities/enumerations/tip-grupe.model';

export interface IGrupa {
  id?: number;
  tipGrupe?: TipGrupe | null;
  detes?: IDete[] | null;
  dnevnik?: IDnevnik | null;
}

export class Grupa implements IGrupa {
  constructor(public id?: number, public tipGrupe?: TipGrupe | null, public detes?: IDete[] | null, public dnevnik?: IDnevnik | null) {}
}

export function getGrupaIdentifier(grupa: IGrupa): number | undefined {
  return grupa.id;
}
