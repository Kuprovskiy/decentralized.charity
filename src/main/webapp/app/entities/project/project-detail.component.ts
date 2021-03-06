import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IProject } from 'app/shared/model/project.model';
import { ITransactions } from 'app/shared/model/transactions.model';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { TransactionsService } from 'app/entities/transactions/transactions.service';
import { ITEMS_PER_PAGE } from 'app/shared';
import { AccountService } from 'app/core';
import { Subscription } from 'rxjs';

import { Label, MultiDataSet } from 'ng2-charts';
import { ChartType } from 'chart.js';

@Component({
  selector: 'jhi-project-detail',
  templateUrl: './project-detail.component.html'
})
export class ProjectDetailComponent implements OnInit {
  project: IProject;
  transactions: ITransactions[];
  links: any;
  public readonly const = 'SUPPLY_CHAIN';
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  routeData: any;
  dataLng = [1, 2, 3];
  currentAccount: any;
  eventSubscriber: Subscription;
  public location = {
    latitude: [],
    longitude: []
  };
  public doughnutChartLabels: Label[] = ['Founded', 'Spend'];
  public doughnutChartData: MultiDataSet = [[]];
  public doughnutChartType: ChartType = 'doughnut';

  title = 'My first AGM project';
  lat = 51.678418;
  lng = 7.809007;
  constructor(
    protected activatedRoute: ActivatedRoute,
    protected transactionsService: TransactionsService,
    protected parseLinks: JhiParseLinks,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  ngOnInit() {
    this.page = 0;
    this.predicate = 'id';
    this.activatedRoute.data.subscribe(({ project }) => {
      this.project = project;
      this.doughnutChartData = [[this.project.amount, 30000]];
    });

    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  loadAll() {
    this.transactionsService
      .query2(
        {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort()
        },
        'api/project/' + this.project.id + '/transactions'
      )
      .subscribe((res: HttpResponse<ITransactions[]>) => {
        console.log(res);
        res.body.forEach(data => {
          if (data && data.longitude && data.latitude) {
            this.location.latitude.push(data.latitude);
            this.location.longitude.push(data.longitude);
            console.log(this.location);
          }
        });
        this.paginateTransactions(res.body, res.headers), (res: HttpErrorResponse) => this.onError(res.message);
      });
  }

  protected paginateTransactions(data: ITransactions[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.transactions = data;
  }

  protected onError(errorMessage: string) {
    // this.jhiAlertService.error(errorMessage, null, null);
  }

  previousState() {
    window.history.back();
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/transactions'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/transactions',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITransactions) {
    return item.id;
  }

  registerChangeInTransactions() {
    this.eventSubscriber = this.eventManager.subscribe('transactionsListModification', response => this.loadAll());
  }
}
