import { IDete } from 'app/entities/dete/dete.model';
import { IFormular } from 'app/entities/formular/formular.model';
import { IUser } from '../../admin/user-management/user-management.model';

export interface IRoditelj {
  id?: number;
  detes?: IDete[] | null;
  formulars?: IFormular[] | null;
  user?: IUser | null;
}

export class Roditelj implements IRoditelj {
  constructor(public id?: number, public detes?: IDete[] | null, public formulars?: IFormular[] | null, user?: IUser | null) {}
}

export function getRoditeljIdentifier(roditelj: IRoditelj): number | undefined {
  return roditelj.id;
}
