import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKorisnik, getKorisnikIdentifier } from '../korisnik.model';

export type EntityResponseType = HttpResponse<IKorisnik>;
export type EntityArrayResponseType = HttpResponse<IKorisnik[]>;

@Injectable({ providedIn: 'root' })
export class KorisnikService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/korisniks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(korisnik: IKorisnik): Observable<EntityResponseType> {
    return this.http.post<IKorisnik>(this.resourceUrl, korisnik, { observe: 'response' });
  }

  update(korisnik: IKorisnik): Observable<EntityResponseType> {
    return this.http.put<IKorisnik>(`${this.resourceUrl}/${getKorisnikIdentifier(korisnik) as number}`, korisnik, { observe: 'response' });
  }

  partialUpdate(korisnik: IKorisnik): Observable<EntityResponseType> {
    return this.http.patch<IKorisnik>(`${this.resourceUrl}/${getKorisnikIdentifier(korisnik) as number}`, korisnik, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKorisnik>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKorisnik[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addKorisnikToCollectionIfMissing(korisnikCollection: IKorisnik[], ...korisniksToCheck: (IKorisnik | null | undefined)[]): IKorisnik[] {
    const korisniks: IKorisnik[] = korisniksToCheck.filter(isPresent);
    if (korisniks.length > 0) {
      const korisnikCollectionIdentifiers = korisnikCollection.map(korisnikItem => getKorisnikIdentifier(korisnikItem)!);
      const korisniksToAdd = korisniks.filter(korisnikItem => {
        const korisnikIdentifier = getKorisnikIdentifier(korisnikItem);
        if (korisnikIdentifier == null || korisnikCollectionIdentifiers.includes(korisnikIdentifier)) {
          return false;
        }
        korisnikCollectionIdentifiers.push(korisnikIdentifier);
        return true;
      });
      return [...korisniksToAdd, ...korisnikCollection];
    }
    return korisnikCollection;
  }
}
