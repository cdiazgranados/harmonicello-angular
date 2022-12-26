import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPresets } from '../presets.model';
import { PresetsService } from '../service/presets.service';

@Injectable({ providedIn: 'root' })
export class PresetsRoutingResolveService implements Resolve<IPresets | null> {
  constructor(protected service: PresetsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPresets | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((presets: HttpResponse<IPresets>) => {
          if (presets.body) {
            return of(presets.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
