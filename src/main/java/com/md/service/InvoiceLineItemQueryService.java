package com.md.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.md.domain.InvoiceLineItem;
import com.md.domain.*; // for static metamodels
import com.md.repository.InvoiceLineItemRepository;
import com.md.service.dto.InvoiceLineItemCriteria;

/**
 * Service for executing complex queries for InvoiceLineItem entities in the database.
 * The main input is a {@link InvoiceLineItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceLineItem} or a {@link Page} of {@link InvoiceLineItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceLineItemQueryService extends QueryService<InvoiceLineItem> {

    private final Logger log = LoggerFactory.getLogger(InvoiceLineItemQueryService.class);

    private final InvoiceLineItemRepository invoiceLineItemRepository;

    public InvoiceLineItemQueryService(InvoiceLineItemRepository invoiceLineItemRepository) {
        this.invoiceLineItemRepository = invoiceLineItemRepository;
    }

    /**
     * Return a {@link List} of {@link InvoiceLineItem} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceLineItem> findByCriteria(InvoiceLineItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InvoiceLineItem> specification = createSpecification(criteria);
        return invoiceLineItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InvoiceLineItem} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceLineItem> findByCriteria(InvoiceLineItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceLineItem> specification = createSpecification(criteria);
        return invoiceLineItemRepository.findAll(specification, page);
    }

    /**
     * Function to convert InvoiceLineItemCriteria to a {@link Specification}
     */
    private Specification<InvoiceLineItem> createSpecification(InvoiceLineItemCriteria criteria) {
        Specification<InvoiceLineItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InvoiceLineItem_.id));
            }
            if (criteria.getInvoiceLineItemNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInvoiceLineItemNumber(), InvoiceLineItem_.invoiceLineItemNumber));
            }
            if (criteria.getProductName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductName(), InvoiceLineItem_.productName));
            }
            if (criteria.getProductQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProductQuantity(), InvoiceLineItem_.productQuantity));
            }
            if (criteria.getProductCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProductCost(), InvoiceLineItem_.productCost));
            }
            if (criteria.getInvoiceNumberId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceNumberId(), InvoiceLineItem_.invoiceNumber, Invoice_.id));
            }
        }
        return specification;
    }
}
