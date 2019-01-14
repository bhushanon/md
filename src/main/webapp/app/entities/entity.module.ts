import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MdInvoiceModule } from './invoice/invoice.module';
import { MdInvoiceLineItemModule } from './invoice-line-item/invoice-line-item.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        MdInvoiceModule,
        MdInvoiceLineItemModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MdEntityModule {}
