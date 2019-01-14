/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MdTestModule } from '../../../test.module';
import { InvoiceLineItemDeleteDialogComponent } from 'app/entities/invoice-line-item/invoice-line-item-delete-dialog.component';
import { InvoiceLineItemService } from 'app/entities/invoice-line-item/invoice-line-item.service';

describe('Component Tests', () => {
    describe('InvoiceLineItem Management Delete Component', () => {
        let comp: InvoiceLineItemDeleteDialogComponent;
        let fixture: ComponentFixture<InvoiceLineItemDeleteDialogComponent>;
        let service: InvoiceLineItemService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MdTestModule],
                declarations: [InvoiceLineItemDeleteDialogComponent]
            })
                .overrideTemplate(InvoiceLineItemDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InvoiceLineItemDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InvoiceLineItemService);
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
