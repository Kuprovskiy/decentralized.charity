import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DecentralizedcharityTestModule } from '../../../test.module';
import { TransactionsDetailComponent } from 'app/entities/transactions/transactions-detail.component';
import { Transactions } from 'app/shared/model/transactions.model';

describe('Component Tests', () => {
  describe('Transactions Management Detail Component', () => {
    let comp: TransactionsDetailComponent;
    let fixture: ComponentFixture<TransactionsDetailComponent>;
    const route = ({ data: of({ transactions: new Transactions(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DecentralizedcharityTestModule],
        declarations: [TransactionsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TransactionsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TransactionsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.transactions).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
