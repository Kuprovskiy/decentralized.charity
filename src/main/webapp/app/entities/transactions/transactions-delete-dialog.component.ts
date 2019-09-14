import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITransactions } from 'app/shared/model/transactions.model';
import { TransactionsService } from './transactions.service';

@Component({
  selector: 'jhi-transactions-delete-dialog',
  templateUrl: './transactions-delete-dialog.component.html'
})
export class TransactionsDeleteDialogComponent {
  transactions: ITransactions;

  constructor(
    protected transactionsService: TransactionsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.transactionsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'transactionsListModification',
        content: 'Deleted an transactions'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-transactions-delete-popup',
  template: ''
})
export class TransactionsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ transactions }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TransactionsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.transactions = transactions;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/transactions', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/transactions', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
