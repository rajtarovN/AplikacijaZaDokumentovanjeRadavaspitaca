import dayjs from 'dayjs/esm';
import { IVaspitac } from 'app/entities/vaspitac/vaspitac.model';
import { IDete } from 'app/entities/dete/dete.model';
import { IUser } from '../../admin/user-management/user-management.model';

export interface IZapazanjeUVeziDeteta {
  id?: number;
  interesovanja?: string | null;
  prednosti?: string | null;
  mane?: string | null;
  predlozi?: string | null;
  datum?: dayjs.Dayjs | null;
  vaspitac?: IVaspitac | null;
  dete?: IDete | null;
  user?: IUser | null;
}

export class ZapazanjeUVeziDeteta implements IZapazanjeUVeziDeteta {
  constructor(
    public id?: number,
    public interesovanja?: string | null,
    public prednosti?: string | null,
    public mane?: string | null,
    public predlozi?: string | null,
    public datum?: dayjs.Dayjs | null,
    public vaspitac?: IVaspitac | null,
    public dete?: IDete | null,
    public user?: IUser | null
  ) {}
}

export function getZapazanjeUVeziDetetaIdentifier(zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta): number | undefined {
  return zapazanjeUVeziDeteta.id;
}
