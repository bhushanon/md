import { Moment } from 'moment';

export interface IInvoice {
    id?: number;
    invoiceNumber?: number;
    invoiceDate?: Moment;
    customerName?: string;
    invoiceAmount?: number;
}

export class Invoice implements IInvoice {
    constructor(
        public id?: number,
        public invoiceNumber?: number,
        public invoiceDate?: Moment,
        public customerName?: string,
        public invoiceAmount?: number
    ) {}
}
