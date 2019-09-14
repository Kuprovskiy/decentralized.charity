import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DecentralizedcharitySharedModule } from 'app/shared/shared.module';
import { TransactionsComponent } from './transactions.component';
import { TransactionsDetailComponent } from './transactions-detail.component';
import { TransactionsUpdateComponent } from './transactions-update.component';
import { TransactionsDeletePopupComponent, TransactionsDeleteDialogComponent } from './transactions-delete-dialog.component';
import { transactionsRoute, transactionsPopupRoute } from './transactions.route';

const ENTITY_STATES = [...transactionsRoute, ...transactionsPopupRoute];

@NgModule({
  imports: [DecentralizedcharitySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TransactionsComponent,
    TransactionsDetailComponent,
    TransactionsUpdateComponent,
    TransactionsDeleteDialogComponent,
    TransactionsDeletePopupComponent
  ],
  entryComponents: [TransactionsComponent, TransactionsUpdateComponent, TransactionsDeleteDialogComponent, TransactionsDeletePopupComponent]
})
export class DecentralizedcharityTransactionsModule {}
