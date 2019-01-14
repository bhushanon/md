package com.md.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.md.domain.InvoiceLineItem;
import com.md.service.InvoiceLineItemService;
import com.md.web.rest.errors.BadRequestAlertException;
import com.md.web.rest.util.HeaderUtil;
import com.md.web.rest.util.PaginationUtil;
import com.md.service.dto.InvoiceLineItemCriteria;
import com.md.service.InvoiceLineItemQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing InvoiceLineItem.
 */
@RestController
@RequestMapping("/api")
public class InvoiceLineItemResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceLineItemResource.class);

    private static final String ENTITY_NAME = "invoiceLineItem";

    private final InvoiceLineItemService invoiceLineItemService;

    private final InvoiceLineItemQueryService invoiceLineItemQueryService;

    public InvoiceLineItemResource(InvoiceLineItemService invoiceLineItemService, InvoiceLineItemQueryService invoiceLineItemQueryService) {
        this.invoiceLineItemService = invoiceLineItemService;
        this.invoiceLineItemQueryService = invoiceLineItemQueryService;
    }

    /**
     * POST  /invoice-line-items : Create a new invoiceLineItem.
     *
     * @param invoiceLineItem the invoiceLineItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceLineItem, or with status 400 (Bad Request) if the invoiceLineItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-line-items")
    @Timed
    public ResponseEntity<InvoiceLineItem> createInvoiceLineItem(@Valid @RequestBody InvoiceLineItem invoiceLineItem) throws URISyntaxException {
        log.debug("REST request to save InvoiceLineItem : {}", invoiceLineItem);
        if (invoiceLineItem.getId() != null) {
            throw new BadRequestAlertException("A new invoiceLineItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceLineItem result = invoiceLineItemService.save(invoiceLineItem);
        return ResponseEntity.created(new URI("/api/invoice-line-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-line-items : Updates an existing invoiceLineItem.
     *
     * @param invoiceLineItem the invoiceLineItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceLineItem,
     * or with status 400 (Bad Request) if the invoiceLineItem is not valid,
     * or with status 500 (Internal Server Error) if the invoiceLineItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-line-items")
    @Timed
    public ResponseEntity<InvoiceLineItem> updateInvoiceLineItem(@Valid @RequestBody InvoiceLineItem invoiceLineItem) throws URISyntaxException {
        log.debug("REST request to update InvoiceLineItem : {}", invoiceLineItem);
        if (invoiceLineItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceLineItem result = invoiceLineItemService.save(invoiceLineItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceLineItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-line-items : get all the invoiceLineItems.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceLineItems in body
     */
    @GetMapping("/invoice-line-items")
    @Timed
    public ResponseEntity<List<InvoiceLineItem>> getAllInvoiceLineItems(InvoiceLineItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InvoiceLineItems by criteria: {}", criteria);
        Page<InvoiceLineItem> page = invoiceLineItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoice-line-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoice-line-items/:id : get the "id" invoiceLineItem.
     *
     * @param id the id of the invoiceLineItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceLineItem, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-line-items/{id}")
    @Timed
    public ResponseEntity<InvoiceLineItem> getInvoiceLineItem(@PathVariable Long id) {
        log.debug("REST request to get InvoiceLineItem : {}", id);
        Optional<InvoiceLineItem> invoiceLineItem = invoiceLineItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceLineItem);
    }

    /**
     * DELETE  /invoice-line-items/:id : delete the "id" invoiceLineItem.
     *
     * @param id the id of the invoiceLineItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-line-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoiceLineItem(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceLineItem : {}", id);
        invoiceLineItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
