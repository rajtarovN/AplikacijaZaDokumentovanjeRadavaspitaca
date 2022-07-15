import dayjs from 'dayjs/esm';
import { IDete } from 'app/entities/dete/dete.model';
import { IDnevnik } from 'app/entities/dnevnik/dnevnik.model';

export interface INeDolasci {
  datum?: dayjs.Dayjs | null;
  razlog?: string | null;
  id?: number;
  dete?: IDete | null;
  dnevnik?: IDnevnik | null;
}

export class NeDolasci implements INeDolasci {
  constructor(
    public datum?: dayjs.Dayjs | null,
    public razlog?: string | null,
    public id?: number,
    public dete?: IDete | null,
    public dnevnik?: IDnevnik | null
  ) {}
}

export function getNeDolasciIdentifier(neDolasci: INeDolasci): number | undefined {
  return neDolasci.id;
}
