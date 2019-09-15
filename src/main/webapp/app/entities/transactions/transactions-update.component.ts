import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ITransactions, Transactions } from 'app/shared/model/transactions.model';
import { TransactionsService } from './transactions.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project/project.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-transactions-update',
  templateUrl: './transactions-update.component.html'
})
export class TransactionsUpdateComponent implements OnInit {
  isSaving: boolean;

  projects: IProject[];

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    txid: [null, [Validators.required]],
    amount: [],
    transactionType: [],
    createdDate: [null, [Validators.required]],
    transactionStatus: [],
    blockHeight: [],
    key: [],
    note: [],
    longitude: [],
    latitude: [],
    humidity: [],
    temperature: [],
    projectId: [],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected transactionsService: TransactionsService,
    protected projectService: ProjectService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ transactions }) => {
      this.updateForm(transactions);
    });
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(transactions: ITransactions) {
    this.editForm.patchValue({
      id: transactions.id,
      txid: transactions.txid,
      amount: transactions.amount,
      transactionType: transactions.transactionType,
      createdDate: transactions.createdDate != null ? transactions.createdDate.format(DATE_TIME_FORMAT) : null,
      transactionStatus: transactions.transactionStatus,
      blockHeight: transactions.blockHeight,
      key: transactions.key,
      note: transactions.note,
      longitude: transactions.longitude,
      latitude: transactions.latitude,
      humidity: transactions.humidity,
      temperature: transactions.temperature,
      projectId: transactions.projectId,
      userId: transactions.userId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const transactions = this.createFromForm();
    if (transactions.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionsService.update(transactions));
    } else {
      this.subscribeToSaveResponse(this.transactionsService.create(transactions));
    }
  }

  private createFromForm(): ITransactions {
    return {
      ...new Transactions(),
      id: this.editForm.get(['id']).value,
      txid: this.editForm.get(['txid']).value,
      amount: this.editForm.get(['amount']).value,
      transactionType: this.editForm.get(['transactionType']).value,
      createdDate:
        this.editForm.get(['createdDate']).value != null ? moment(this.editForm.get(['createdDate']).value, DATE_TIME_FORMAT) : undefined,
      transactionStatus: this.editForm.get(['transactionStatus']).value,
      blockHeight: this.editForm.get(['blockHeight']).value,
      key: this.editForm.get(['key']).value,
      note: this.editForm.get(['note']).value,
      longitude: this.editForm.get(['longitude']).value,
      latitude: this.editForm.get(['latitude']).value,
      humidity: this.editForm.get(['humidity']).value,
      temperature: this.editForm.get(['temperature']).value,
      projectId: this.editForm.get(['projectId']).value,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactions>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackProjectById(index: number, item: IProject) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
