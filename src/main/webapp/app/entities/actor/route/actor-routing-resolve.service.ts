import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, Router} from '@angular/router';
import {EMPTY, Observable, of} from 'rxjs';
import {mergeMap} from 'rxjs/operators';

import {Actor, IActor} from '../actor.model';
import {ActorService} from '../service/actor.service';

@Injectable({ providedIn: 'root' })
export class ActorRoutingResolveService implements Resolve<IActor> {
  constructor(protected service: ActorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IActor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((actor: HttpResponse<Actor>) => {
          if (actor.body) {
            return of(actor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Actor());
  }
}