import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceLineItem } from 'app/shared/model/invoice-line-item.model';

@Component({
    selector: 'jhi-invoice-line-item-detail',
    templateUrl: './invoice-line-item-detail.component.html'
})
export class InvoiceLineItemDetailComponent implements OnInit {
    invoiceLineItem: IInvoiceLineItem;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ invoiceLineItem }) => {
            this.invoiceLineItem = invoiceLineItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
