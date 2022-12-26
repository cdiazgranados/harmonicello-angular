import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPresets, NewPresets } from '../presets.model';

export type PartialUpdatePresets = Partial<IPresets> & Pick<IPresets, 'id'>;

export type EntityResponseType = HttpResponse<IPresets>;
export type EntityArrayResponseType = HttpResponse<IPresets[]>;

@Injectable({ providedIn: 'root' })
export class PresetsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/presets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(presets: NewPresets): Observable<EntityResponseType> {
    return this.http.post<IPresets>(this.resourceUrl, presets, { observe: 'response' });
  }

  update(presets: IPresets): Observable<EntityResponseType> {
    return this.http.put<IPresets>(`${this.resourceUrl}/${this.getPresetsIdentifier(presets)}`, presets, { observe: 'response' });
  }

  partialUpdate(presets: PartialUpdatePresets): Observable<EntityResponseType> {
    return this.http.patch<IPresets>(`${this.resourceUrl}/${this.getPresetsIdentifier(presets)}`, presets, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPresets>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPresets[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPresetsIdentifier(presets: Pick<IPresets, 'id'>): number {
    return presets.id;
  }

  comparePresets(o1: Pick<IPresets, 'id'> | null, o2: Pick<IPresets, 'id'> | null): boolean {
    return o1 && o2 ? this.getPresetsIdentifier(o1) === this.getPresetsIdentifier(o2) : o1 === o2;
  }

  addPresetsToCollectionIfMissing<Type extends Pick<IPresets, 'id'>>(
    presetsCollection: Type[],
    ...presetsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const presets: Type[] = presetsToCheck.filter(isPresent);
    if (presets.length > 0) {
      const presetsCollectionIdentifiers = presetsCollection.map(presetsItem => this.getPresetsIdentifier(presetsItem)!);
      const presetsToAdd = presets.filter(presetsItem => {
        const presetsIdentifier = this.getPresetsIdentifier(presetsItem);
        if (presetsCollectionIdentifiers.includes(presetsIdentifier)) {
          return false;
        }
        presetsCollectionIdentifiers.push(presetsIdentifier);
        return true;
      });
      return [...presetsToAdd, ...presetsCollection];
    }
    return presetsCollection;
  }
}
