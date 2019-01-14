import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInvoiceLineItem } from 'app/shared/model/invoice-line-item.model';
import { InvoiceLineItemService } from './invoice-line-item.service';

@Component({
    selector: 'jhi-invoice-line-item-delete-dialog',
    templateUrl: './invoice-line-item-delete-dialog.component.html'
})
export class InvoiceLineItemDeleteDialogComponent {
    invoiceLineItem: IInvoiceLineItem;

    constructor(
        private invoiceLineItemService: InvoiceLineItemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.invoiceLineItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'invoiceLineItemListModification',
                content: 'Deleted an invoiceLineItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-invoice-line-item-delete-popup',
    template: ''
})
export class InvoiceLineItemDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ invoiceLineItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(InvoiceLineItemDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.invoiceLineItem = invoiceLineItem;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
