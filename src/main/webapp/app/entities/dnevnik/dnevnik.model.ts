import dayjs from 'dayjs/esm';
import { IGrupa } from 'app/entities/grupa/grupa.model';
import { IPrica } from 'app/entities/prica/prica.model';
import { INeDolasci } from 'app/entities/ne-dolasci/ne-dolasci.model';
import { IVaspitac } from 'app/entities/vaspitac/vaspitac.model';

export interface IDnevnik {
  pocetakVazenja?: dayjs.Dayjs | null;
  krajVazenja?: dayjs.Dayjs | null;
  id?: number;
  grupa?: IGrupa | null;
  pricas?: IPrica[] | null;
  neDolascis?: INeDolasci[] | null;
  vaspitacs?: IVaspitac[] | null;
}

export class Dnevnik implements IDnevnik {
  constructor(
    public pocetakVazenja?: dayjs.Dayjs | null,
    public krajVazenja?: dayjs.Dayjs | null,
    public id?: number,
    public grupa?: IGrupa | null,
    public pricas?: IPrica[] | null,
    public neDolascis?: INeDolasci[] | null,
    public vaspitacs?: IVaspitac[] | null
  ) {}
}

export function getDnevnikIdentifier(dnevnik: IDnevnik): number | undefined {
  return dnevnik.id;
}
