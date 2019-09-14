import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITransactions } from 'app/shared/model/transactions.model';

type EntityResponseType = HttpResponse<ITransactions>;
type EntityArrayResponseType = HttpResponse<ITransactions[]>;

@Injectable({ providedIn: 'root' })
export class TransactionsService {
  public resourceUrl = SERVER_API_URL + 'api/transactions';

  constructor(protected http: HttpClient) {}

  create(transactions: ITransactions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transactions);
    return this.http
      .post<ITransactions>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(transactions: ITransactions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transactions);
    return this.http
      .put<ITransactions>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITransactions>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransactions[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query2(req?: any, query?: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransactions[]>(query, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(transactions: ITransactions): ITransactions {
    const copy: ITransactions = Object.assign({}, transactions, {
      createdDate: transactions.createdDate != null && transactions.createdDate.isValid() ? transactions.createdDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((transactions: ITransactions) => {
        transactions.createdDate = transactions.createdDate != null ? moment(transactions.createdDate) : null;
      });
    }
    return res;
  }
}
