import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReview, Review } from '../review.model';
import { ReviewService } from '../service/review.service';

@Injectable({ providedIn: 'root' })
export class ReviewRoutingResolveService implements Resolve<IReview> {
  constructor(protected service: ReviewService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReview> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((review: HttpResponse<Review>) => {
          if (review.body) {
            return of(review.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Review());
  }
}
