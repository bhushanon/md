package com.md.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A InvoiceLineItem.
 */
@Entity
@Table(name = "invoice_line_item")
public class InvoiceLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "invoice_line_item_number", nullable = false)
    private Integer invoiceLineItemNumber;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Min(value = 1)
    @Column(name = "product_quantity", nullable = false)
    private Integer productQuantity;

    @NotNull
    @Min(value = 1)
    @Column(name = "product_cost", nullable = false)
    private Integer productCost;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Invoice invoiceNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInvoiceLineItemNumber() {
        return invoiceLineItemNumber;
    }

    public InvoiceLineItem invoiceLineItemNumber(Integer invoiceLineItemNumber) {
        this.invoiceLineItemNumber = invoiceLineItemNumber;
        return this;
    }

    public void setInvoiceLineItemNumber(Integer invoiceLineItemNumber) {
        this.invoiceLineItemNumber = invoiceLineItemNumber;
    }

    public String getProductName() {
        return productName;
    }

    public InvoiceLineItem productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public InvoiceLineItem productQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
        return this;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Integer getProductCost() {
        return productCost;
    }

    public InvoiceLineItem productCost(Integer productCost) {
        this.productCost = productCost;
        return this;
    }

    public void setProductCost(Integer productCost) {
        this.productCost = productCost;
    }

    public Invoice getInvoiceNumber() {
        return invoiceNumber;
    }

    public InvoiceLineItem invoiceNumber(Invoice invoice) {
        this.invoiceNumber = invoice;
        return this;
    }

    public void setInvoiceNumber(Invoice invoice) {
        this.invoiceNumber = invoice;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvoiceLineItem invoiceLineItem = (InvoiceLineItem) o;
        if (invoiceLineItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceLineItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceLineItem{" +
            "id=" + getId() +
            ", invoiceLineItemNumber=" + getInvoiceLineItemNumber() +
            ", productName='" + getProductName() + "'" +
            ", productQuantity=" + getProductQuantity() +
            ", productCost=" + getProductCost() +
            "}";
    }
}
