import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DecentralizedcharityTestModule } from '../../../test.module';
import { TransactionsDeleteDialogComponent } from 'app/entities/transactions/transactions-delete-dialog.component';
import { TransactionsService } from 'app/entities/transactions/transactions.service';

describe('Component Tests', () => {
  describe('Transactions Management Delete Component', () => {
    let comp: TransactionsDeleteDialogComponent;
    let fixture: ComponentFixture<TransactionsDeleteDialogComponent>;
    let service: TransactionsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DecentralizedcharityTestModule],
        declarations: [TransactionsDeleteDialogComponent]
      })
        .overrideTemplate(TransactionsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TransactionsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TransactionsService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
