import dayjs from 'dayjs/esm';

export interface IPlanPrice {
  id?: number;
  izvoriSaznanja?: string | null;
  nazivTeme?: string | null;
  pocetnaIdeja?: string | null;
  datumPocetka?: dayjs.Dayjs | null;
  nacinUcescaVaspitaca?: string | null;
  materijali?: string | null;
  ucescePorodice?: string | null;
  mesta?: string | null;
  datumZavrsetka?: dayjs.Dayjs | null;
}

export class PlanPrice implements IPlanPrice {
  constructor(
    public id?: number,
    public izvoriSaznanja?: string | null,
    public nazivTeme?: string | null,
    public pocetnaIdeja?: string | null,
    public datumPocetka?: dayjs.Dayjs | null,
    public nacinUcescaVaspitaca?: string | null,
    public materijali?: string | null,
    public ucescePorodice?: string | null,
    public mesta?: string | null,
    public datumZavrsetka?: dayjs.Dayjs | null
  ) {}
}

export function getPlanPriceIdentifier(planPrice: IPlanPrice): number | undefined {
  return planPrice.id;
}
