import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MdSharedModule } from 'app/shared';
import {
    InvoiceLineItemComponent,
    InvoiceLineItemDetailComponent,
    InvoiceLineItemNewComponent,
    InvoiceLineItemUpdateComponent,
    InvoiceLineItemDeletePopupComponent,
    InvoiceLineItemDeleteDialogComponent,
    invoiceLineItemRoute,
    invoiceLineItemPopupRoute
} from './';

const ENTITY_STATES = [...invoiceLineItemRoute, ...invoiceLineItemPopupRoute];

@NgModule({
    imports: [MdSharedModule, RouterModule.forChild(ENTITY_STATES)],
    exports: [
        InvoiceLineItemComponent,
        InvoiceLineItemDetailComponent,
        InvoiceLineItemNewComponent,
        InvoiceLineItemUpdateComponent,
        InvoiceLineItemDeleteDialogComponent,
        InvoiceLineItemDeletePopupComponent
    ],
    declarations: [
        InvoiceLineItemComponent,
        InvoiceLineItemDetailComponent,
        InvoiceLineItemNewComponent,
        InvoiceLineItemUpdateComponent,
        InvoiceLineItemDeleteDialogComponent,
        InvoiceLineItemDeletePopupComponent
    ],
    entryComponents: [
        InvoiceLineItemComponent,
        InvoiceLineItemNewComponent,
        InvoiceLineItemUpdateComponent,
        InvoiceLineItemDeleteDialogComponent,
        InvoiceLineItemDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MdInvoiceLineItemModule {}
