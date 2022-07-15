import { IObjekat } from 'app/entities/objekat/objekat.model';
import { StatusMaterijala } from 'app/entities/enumerations/status-materijala.model';

export interface IPotrebanMaterijal {
  naziv?: string | null;
  kolicina?: number | null;
  id?: number;
  statusMaterijala?: StatusMaterijala | null;
  objekat?: IObjekat | null;
}

export class PotrebanMaterijal implements IPotrebanMaterijal {
  constructor(
    public naziv?: string | null,
    public kolicina?: number | null,
    public id?: number,
    public statusMaterijala?: StatusMaterijala | null,
    public objekat?: IObjekat | null
  ) {}
}

export function getPotrebanMaterijalIdentifier(potrebanMaterijal: IPotrebanMaterijal): number | undefined {
  return potrebanMaterijal.id;
}
