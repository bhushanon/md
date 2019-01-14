import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { InvoiceLineItem } from 'app/shared/model/invoice-line-item.model';
import { InvoiceLineItemService } from './invoice-line-item.service';
import { InvoiceLineItemComponent } from './invoice-line-item.component';
import { InvoiceLineItemDetailComponent } from './invoice-line-item-detail.component';
import { InvoiceLineItemUpdateComponent } from './invoice-line-item-update.component';
import { InvoiceLineItemDeletePopupComponent } from './invoice-line-item-delete-dialog.component';
import { IInvoiceLineItem } from 'app/shared/model/invoice-line-item.model';

@Injectable({ providedIn: 'root' })
export class InvoiceLineItemResolve implements Resolve<IInvoiceLineItem> {
    constructor(private service: InvoiceLineItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((invoiceLineItem: HttpResponse<InvoiceLineItem>) => invoiceLineItem.body));
        }
        return of(new InvoiceLineItem());
    }
}

export const invoiceLineItemRoute: Routes = [
    {
        path: 'invoice-line-item',
        component: InvoiceLineItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mdApp.invoiceLineItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice-line-item/:id/view',
        component: InvoiceLineItemDetailComponent,
        resolve: {
            invoiceLineItem: InvoiceLineItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mdApp.invoiceLineItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice-line-item/new',
        component: InvoiceLineItemUpdateComponent,
        resolve: {
            invoiceLineItem: InvoiceLineItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mdApp.invoiceLineItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice-line-item/:id/edit',
        component: InvoiceLineItemUpdateComponent,
        resolve: {
            invoiceLineItem: InvoiceLineItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mdApp.invoiceLineItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const invoiceLineItemPopupRoute: Routes = [
    {
        path: 'invoice-line-item/:id/delete',
        component: InvoiceLineItemDeletePopupComponent,
        resolve: {
            invoiceLineItem: InvoiceLineItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mdApp.invoiceLineItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
