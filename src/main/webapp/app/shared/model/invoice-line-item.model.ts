import { IInvoice } from 'app/shared/model//invoice.model';

export interface IInvoiceLineItem {
    id?: number;
    invoiceLineItemNumber?: number;
    productName?: string;
    productQuantity?: number;
    productCost?: number;
    invoiceNumber?: IInvoice;
}

export class InvoiceLineItem implements IInvoiceLineItem {
    constructor(
        public id?: number,
        public invoiceLineItemNumber?: number,
        public productName?: string,
        public productQuantity?: number,
        public productCost?: number,
        public invoiceNumber?: IInvoice
    ) {}
}
