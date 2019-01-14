import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInvoiceLineItem } from 'app/shared/model/invoice-line-item.model';

type EntityResponseType = HttpResponse<IInvoiceLineItem>;
type EntityArrayResponseType = HttpResponse<IInvoiceLineItem[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceLineItemService {
    private resourceUrl = SERVER_API_URL + 'api/invoice-line-items';

    constructor(private http: HttpClient) {}

    create(invoiceLineItem: IInvoiceLineItem): Observable<EntityResponseType> {
        return this.http.post<IInvoiceLineItem>(this.resourceUrl, invoiceLineItem, { observe: 'response' });
    }

    update(invoiceLineItem: IInvoiceLineItem): Observable<EntityResponseType> {
        return this.http.put<IInvoiceLineItem>(this.resourceUrl, invoiceLineItem, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IInvoiceLineItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInvoiceLineItem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
