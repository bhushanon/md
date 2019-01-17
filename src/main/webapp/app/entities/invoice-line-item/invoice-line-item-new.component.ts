import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IInvoiceLineItem } from 'app/shared/model/invoice-line-item.model';
import { InvoiceLineItemService } from './invoice-line-item.service';
import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from 'app/entities/invoice';

@Component({
    selector: 'jhi-invoice-line-item-new',
    templateUrl: './invoice-line-item-new.component.html'
})
export class InvoiceLineItemNewComponent implements OnInit {
    private _invoiceLineItem: IInvoiceLineItem;
    isSaving: boolean;
    invoiceId: number;
    invoices: IInvoice[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private invoiceLineItemService: InvoiceLineItemService,
        private invoiceService: InvoiceService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.params.subscribe(params => {
            this.invoiceId = +params['invoiceId'];
         });

        this.activatedRoute.data.subscribe(({ invoiceLineItem }) => {
            this.invoiceLineItem = invoiceLineItem;
        });
        this.invoiceService.query({
            'id.equals':	this.invoiceId
        }).subscribe(
            (res: HttpResponse<IInvoice[]>) => {
                this.invoices = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.invoiceLineItem.id !== undefined) {
            this.subscribeToSaveResponse(this.invoiceLineItemService.update(this.invoiceLineItem));
        } else {
            this.subscribeToSaveResponse(this.invoiceLineItemService.create(this.invoiceLineItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceLineItem>>) {
        result.subscribe((res: HttpResponse<IInvoiceLineItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackInvoiceById(index: number, item: IInvoice) {
        return item.id;
    }
    get invoiceLineItem() {
        return this._invoiceLineItem;
    }

    set invoiceLineItem(invoiceLineItem: IInvoiceLineItem) {
        this._invoiceLineItem = invoiceLineItem;
    }
}
