package com.md.service;

import com.md.domain.InvoiceLineItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing InvoiceLineItem.
 */
public interface InvoiceLineItemService {

    /**
     * Save a invoiceLineItem.
     *
     * @param invoiceLineItem the entity to save
     * @return the persisted entity
     */
    InvoiceLineItem save(InvoiceLineItem invoiceLineItem);

    /**
     * Get all the invoiceLineItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InvoiceLineItem> findAll(Pageable pageable);


    /**
     * Get the "id" invoiceLineItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InvoiceLineItem> findOne(Long id);

    /**
     * Delete the "id" invoiceLineItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
