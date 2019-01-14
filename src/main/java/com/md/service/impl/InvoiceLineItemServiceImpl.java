package com.md.service.impl;

import com.md.service.InvoiceLineItemService;
import com.md.domain.InvoiceLineItem;
import com.md.repository.InvoiceLineItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing InvoiceLineItem.
 */
@Service
@Transactional
public class InvoiceLineItemServiceImpl implements InvoiceLineItemService {

    private final Logger log = LoggerFactory.getLogger(InvoiceLineItemServiceImpl.class);

    private final InvoiceLineItemRepository invoiceLineItemRepository;

    public InvoiceLineItemServiceImpl(InvoiceLineItemRepository invoiceLineItemRepository) {
        this.invoiceLineItemRepository = invoiceLineItemRepository;
    }

    /**
     * Save a invoiceLineItem.
     *
     * @param invoiceLineItem the entity to save
     * @return the persisted entity
     */
    @Override
    public InvoiceLineItem save(InvoiceLineItem invoiceLineItem) {
        log.debug("Request to save InvoiceLineItem : {}", invoiceLineItem);        return invoiceLineItemRepository.save(invoiceLineItem);
    }

    /**
     * Get all the invoiceLineItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceLineItem> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceLineItems");
        return invoiceLineItemRepository.findAll(pageable);
    }


    /**
     * Get one invoiceLineItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceLineItem> findOne(Long id) {
        log.debug("Request to get InvoiceLineItem : {}", id);
        return invoiceLineItemRepository.findById(id);
    }

    /**
     * Delete the invoiceLineItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InvoiceLineItem : {}", id);
        invoiceLineItemRepository.deleteById(id);
    }
}
