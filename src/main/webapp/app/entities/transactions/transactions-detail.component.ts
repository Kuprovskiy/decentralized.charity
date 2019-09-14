import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransactions } from 'app/shared/model/transactions.model';

@Component({
  selector: 'jhi-transactions-detail',
  templateUrl: './transactions-detail.component.html'
})
export class TransactionsDetailComponent implements OnInit {
  transactions: ITransactions;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ transactions }) => {
      this.transactions = transactions;
    });
  }

  previousState() {
    window.history.back();
  }
}
