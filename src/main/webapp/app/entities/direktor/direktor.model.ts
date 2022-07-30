import { IUser } from '../../admin/user-management/user-management.model';

export interface IDirektor {
  id?: number;
  user?: IUser;
}

export class Direktor implements IDirektor {
  constructor(public id?: number, public user?: IUser) {}
}

export function getDirektorIdentifier(direktor: IDirektor): number | undefined {
  return direktor.id;
}
