import { IUser } from '../../admin/user-management/user-management.model';

export interface IPedagog {
  id?: number;
  user?: IUser;
}

export class Pedagog implements IPedagog {
  constructor(public id?: number, public user?: IUser) {}
}

export function getPedagogIdentifier(pedagog: IPedagog): number | undefined {
  return pedagog.id;
}
