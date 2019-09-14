import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Transactions } from 'app/shared/model/transactions.model';
import { TransactionsService } from './transactions.service';
import { TransactionsComponent } from './transactions.component';
import { TransactionsDetailComponent } from './transactions-detail.component';
import { TransactionsUpdateComponent } from './transactions-update.component';
import { TransactionsDeletePopupComponent } from './transactions-delete-dialog.component';
import { ITransactions } from 'app/shared/model/transactions.model';

@Injectable({ providedIn: 'root' })
export class TransactionsResolve implements Resolve<ITransactions> {
  constructor(private service: TransactionsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITransactions> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Transactions>) => response.ok),
        map((transactions: HttpResponse<Transactions>) => transactions.body)
      );
    }
    return of(new Transactions());
  }
}

export const transactionsRoute: Routes = [
  {
    path: '',
    component: TransactionsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'decentralizedcharityApp.transactions.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TransactionsDetailComponent,
    resolve: {
      transactions: TransactionsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'decentralizedcharityApp.transactions.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TransactionsUpdateComponent,
    resolve: {
      transactions: TransactionsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'decentralizedcharityApp.transactions.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TransactionsUpdateComponent,
    resolve: {
      transactions: TransactionsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'decentralizedcharityApp.transactions.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const transactionsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TransactionsDeletePopupComponent,
    resolve: {
      transactions: TransactionsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'decentralizedcharityApp.transactions.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
