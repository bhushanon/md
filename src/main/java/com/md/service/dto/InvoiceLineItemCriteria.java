package com.md.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the InvoiceLineItem entity. This class is used in InvoiceLineItemResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /invoice-line-items?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InvoiceLineItemCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter invoiceLineItemNumber;

    private StringFilter productName;

    private IntegerFilter productQuantity;

    private IntegerFilter productCost;

    private LongFilter invoiceNumberId;

    public InvoiceLineItemCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getInvoiceLineItemNumber() {
        return invoiceLineItemNumber;
    }

    public void setInvoiceLineItemNumber(IntegerFilter invoiceLineItemNumber) {
        this.invoiceLineItemNumber = invoiceLineItemNumber;
    }

    public StringFilter getProductName() {
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public IntegerFilter getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(IntegerFilter productQuantity) {
        this.productQuantity = productQuantity;
    }

    public IntegerFilter getProductCost() {
        return productCost;
    }

    public void setProductCost(IntegerFilter productCost) {
        this.productCost = productCost;
    }

    public LongFilter getInvoiceNumberId() {
        return invoiceNumberId;
    }

    public void setInvoiceNumberId(LongFilter invoiceNumberId) {
        this.invoiceNumberId = invoiceNumberId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InvoiceLineItemCriteria that = (InvoiceLineItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(invoiceLineItemNumber, that.invoiceLineItemNumber) &&
            Objects.equals(productName, that.productName) &&
            Objects.equals(productQuantity, that.productQuantity) &&
            Objects.equals(productCost, that.productCost) &&
            Objects.equals(invoiceNumberId, that.invoiceNumberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        invoiceLineItemNumber,
        productName,
        productQuantity,
        productCost,
        invoiceNumberId
        );
    }

    @Override
    public String toString() {
        return "InvoiceLineItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (invoiceLineItemNumber != null ? "invoiceLineItemNumber=" + invoiceLineItemNumber + ", " : "") +
                (productName != null ? "productName=" + productName + ", " : "") +
                (productQuantity != null ? "productQuantity=" + productQuantity + ", " : "") +
                (productCost != null ? "productCost=" + productCost + ", " : "") +
                (invoiceNumberId != null ? "invoiceNumberId=" + invoiceNumberId + ", " : "") +
            "}";
    }

}
