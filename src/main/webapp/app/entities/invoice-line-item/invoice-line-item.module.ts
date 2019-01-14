import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MdSharedModule } from 'app/shared';
import {
    InvoiceLineItemComponent,
    InvoiceLineItemDetailComponent,
    InvoiceLineItemUpdateComponent,
    InvoiceLineItemDeletePopupComponent,
    InvoiceLineItemDeleteDialogComponent,
    invoiceLineItemRoute,
    invoiceLineItemPopupRoute
} from './';

const ENTITY_STATES = [...invoiceLineItemRoute, ...invoiceLineItemPopupRoute];

@NgModule({
    imports: [MdSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        InvoiceLineItemComponent,
        InvoiceLineItemDetailComponent,
        InvoiceLineItemUpdateComponent,
        InvoiceLineItemDeleteDialogComponent,
        InvoiceLineItemDeletePopupComponent
    ],
    entryComponents: [
        InvoiceLineItemComponent,
        InvoiceLineItemUpdateComponent,
        InvoiceLineItemDeleteDialogComponent,
        InvoiceLineItemDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MdInvoiceLineItemModule {}
