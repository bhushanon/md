/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MdTestModule } from '../../../test.module';
import { InvoiceLineItemUpdateComponent } from 'app/entities/invoice-line-item/invoice-line-item-update.component';
import { InvoiceLineItemService } from 'app/entities/invoice-line-item/invoice-line-item.service';
import { InvoiceLineItem } from 'app/shared/model/invoice-line-item.model';

describe('Component Tests', () => {
    describe('InvoiceLineItem Management Update Component', () => {
        let comp: InvoiceLineItemUpdateComponent;
        let fixture: ComponentFixture<InvoiceLineItemUpdateComponent>;
        let service: InvoiceLineItemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MdTestModule],
                declarations: [InvoiceLineItemUpdateComponent]
            })
                .overrideTemplate(InvoiceLineItemUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InvoiceLineItemUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InvoiceLineItemService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new InvoiceLineItem(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.invoiceLineItem = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new InvoiceLineItem();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.invoiceLineItem = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
