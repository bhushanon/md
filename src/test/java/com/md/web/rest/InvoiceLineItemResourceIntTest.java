package com.md.web.rest;

import com.md.MdApp;

import com.md.domain.InvoiceLineItem;
import com.md.domain.Invoice;
import com.md.repository.InvoiceLineItemRepository;
import com.md.service.InvoiceLineItemService;
import com.md.web.rest.errors.ExceptionTranslator;
import com.md.service.dto.InvoiceLineItemCriteria;
import com.md.service.InvoiceLineItemQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.md.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InvoiceLineItemResource REST controller.
 *
 * @see InvoiceLineItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MdApp.class)
public class InvoiceLineItemResourceIntTest {

    private static final Integer DEFAULT_INVOICE_LINE_ITEM_NUMBER = 1;
    private static final Integer UPDATED_INVOICE_LINE_ITEM_NUMBER = 2;

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRODUCT_QUANTITY = 1;
    private static final Integer UPDATED_PRODUCT_QUANTITY = 2;

    private static final Integer DEFAULT_PRODUCT_COST = 1;
    private static final Integer UPDATED_PRODUCT_COST = 2;

    @Autowired
    private InvoiceLineItemRepository invoiceLineItemRepository;
    
    @Autowired
    private InvoiceLineItemService invoiceLineItemService;

    @Autowired
    private InvoiceLineItemQueryService invoiceLineItemQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceLineItemMockMvc;

    private InvoiceLineItem invoiceLineItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceLineItemResource invoiceLineItemResource = new InvoiceLineItemResource(invoiceLineItemService, invoiceLineItemQueryService);
        this.restInvoiceLineItemMockMvc = MockMvcBuilders.standaloneSetup(invoiceLineItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceLineItem createEntity(EntityManager em) {
        InvoiceLineItem invoiceLineItem = new InvoiceLineItem()
            .invoiceLineItemNumber(DEFAULT_INVOICE_LINE_ITEM_NUMBER)
            .productName(DEFAULT_PRODUCT_NAME)
            .productQuantity(DEFAULT_PRODUCT_QUANTITY)
            .productCost(DEFAULT_PRODUCT_COST);
        // Add required entity
        Invoice invoice = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoice);
        em.flush();
        invoiceLineItem.setInvoiceNumber(invoice);
        return invoiceLineItem;
    }

    @Before
    public void initTest() {
        invoiceLineItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceLineItem() throws Exception {
        int databaseSizeBeforeCreate = invoiceLineItemRepository.findAll().size();

        // Create the InvoiceLineItem
        restInvoiceLineItemMockMvc.perform(post("/api/invoice-line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem)))
            .andExpect(status().isCreated());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceLineItem testInvoiceLineItem = invoiceLineItemList.get(invoiceLineItemList.size() - 1);
        assertThat(testInvoiceLineItem.getInvoiceLineItemNumber()).isEqualTo(DEFAULT_INVOICE_LINE_ITEM_NUMBER);
        assertThat(testInvoiceLineItem.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testInvoiceLineItem.getProductQuantity()).isEqualTo(DEFAULT_PRODUCT_QUANTITY);
        assertThat(testInvoiceLineItem.getProductCost()).isEqualTo(DEFAULT_PRODUCT_COST);
    }

    @Test
    @Transactional
    public void createInvoiceLineItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceLineItemRepository.findAll().size();

        // Create the InvoiceLineItem with an existing ID
        invoiceLineItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceLineItemMockMvc.perform(post("/api/invoice-line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInvoiceLineItemNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineItemRepository.findAll().size();
        // set the field null
        invoiceLineItem.setInvoiceLineItemNumber(null);

        // Create the InvoiceLineItem, which fails.

        restInvoiceLineItemMockMvc.perform(post("/api/invoice-line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineItemRepository.findAll().size();
        // set the field null
        invoiceLineItem.setProductName(null);

        // Create the InvoiceLineItem, which fails.

        restInvoiceLineItemMockMvc.perform(post("/api/invoice-line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineItemRepository.findAll().size();
        // set the field null
        invoiceLineItem.setProductQuantity(null);

        // Create the InvoiceLineItem, which fails.

        restInvoiceLineItemMockMvc.perform(post("/api/invoice-line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineItemRepository.findAll().size();
        // set the field null
        invoiceLineItem.setProductCost(null);

        // Create the InvoiceLineItem, which fails.

        restInvoiceLineItemMockMvc.perform(post("/api/invoice-line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItems() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList
        restInvoiceLineItemMockMvc.perform(get("/api/invoice-line-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceLineItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceLineItemNumber").value(hasItem(DEFAULT_INVOICE_LINE_ITEM_NUMBER)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].productQuantity").value(hasItem(DEFAULT_PRODUCT_QUANTITY)))
            .andExpect(jsonPath("$.[*].productCost").value(hasItem(DEFAULT_PRODUCT_COST)));
    }
    
    @Test
    @Transactional
    public void getInvoiceLineItem() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get the invoiceLineItem
        restInvoiceLineItemMockMvc.perform(get("/api/invoice-line-items/{id}", invoiceLineItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceLineItem.getId().intValue()))
            .andExpect(jsonPath("$.invoiceLineItemNumber").value(DEFAULT_INVOICE_LINE_ITEM_NUMBER))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME.toString()))
            .andExpect(jsonPath("$.productQuantity").value(DEFAULT_PRODUCT_QUANTITY))
            .andExpect(jsonPath("$.productCost").value(DEFAULT_PRODUCT_COST));
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByInvoiceLineItemNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where invoiceLineItemNumber equals to DEFAULT_INVOICE_LINE_ITEM_NUMBER
        defaultInvoiceLineItemShouldBeFound("invoiceLineItemNumber.equals=" + DEFAULT_INVOICE_LINE_ITEM_NUMBER);

        // Get all the invoiceLineItemList where invoiceLineItemNumber equals to UPDATED_INVOICE_LINE_ITEM_NUMBER
        defaultInvoiceLineItemShouldNotBeFound("invoiceLineItemNumber.equals=" + UPDATED_INVOICE_LINE_ITEM_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByInvoiceLineItemNumberIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where invoiceLineItemNumber in DEFAULT_INVOICE_LINE_ITEM_NUMBER or UPDATED_INVOICE_LINE_ITEM_NUMBER
        defaultInvoiceLineItemShouldBeFound("invoiceLineItemNumber.in=" + DEFAULT_INVOICE_LINE_ITEM_NUMBER + "," + UPDATED_INVOICE_LINE_ITEM_NUMBER);

        // Get all the invoiceLineItemList where invoiceLineItemNumber equals to UPDATED_INVOICE_LINE_ITEM_NUMBER
        defaultInvoiceLineItemShouldNotBeFound("invoiceLineItemNumber.in=" + UPDATED_INVOICE_LINE_ITEM_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByInvoiceLineItemNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where invoiceLineItemNumber is not null
        defaultInvoiceLineItemShouldBeFound("invoiceLineItemNumber.specified=true");

        // Get all the invoiceLineItemList where invoiceLineItemNumber is null
        defaultInvoiceLineItemShouldNotBeFound("invoiceLineItemNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByInvoiceLineItemNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where invoiceLineItemNumber greater than or equals to DEFAULT_INVOICE_LINE_ITEM_NUMBER
        defaultInvoiceLineItemShouldBeFound("invoiceLineItemNumber.greaterOrEqualThan=" + DEFAULT_INVOICE_LINE_ITEM_NUMBER);

        // Get all the invoiceLineItemList where invoiceLineItemNumber greater than or equals to UPDATED_INVOICE_LINE_ITEM_NUMBER
        defaultInvoiceLineItemShouldNotBeFound("invoiceLineItemNumber.greaterOrEqualThan=" + UPDATED_INVOICE_LINE_ITEM_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByInvoiceLineItemNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where invoiceLineItemNumber less than or equals to DEFAULT_INVOICE_LINE_ITEM_NUMBER
        defaultInvoiceLineItemShouldNotBeFound("invoiceLineItemNumber.lessThan=" + DEFAULT_INVOICE_LINE_ITEM_NUMBER);

        // Get all the invoiceLineItemList where invoiceLineItemNumber less than or equals to UPDATED_INVOICE_LINE_ITEM_NUMBER
        defaultInvoiceLineItemShouldBeFound("invoiceLineItemNumber.lessThan=" + UPDATED_INVOICE_LINE_ITEM_NUMBER);
    }


    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productName equals to DEFAULT_PRODUCT_NAME
        defaultInvoiceLineItemShouldBeFound("productName.equals=" + DEFAULT_PRODUCT_NAME);

        // Get all the invoiceLineItemList where productName equals to UPDATED_PRODUCT_NAME
        defaultInvoiceLineItemShouldNotBeFound("productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productName in DEFAULT_PRODUCT_NAME or UPDATED_PRODUCT_NAME
        defaultInvoiceLineItemShouldBeFound("productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME);

        // Get all the invoiceLineItemList where productName equals to UPDATED_PRODUCT_NAME
        defaultInvoiceLineItemShouldNotBeFound("productName.in=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productName is not null
        defaultInvoiceLineItemShouldBeFound("productName.specified=true");

        // Get all the invoiceLineItemList where productName is null
        defaultInvoiceLineItemShouldNotBeFound("productName.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productQuantity equals to DEFAULT_PRODUCT_QUANTITY
        defaultInvoiceLineItemShouldBeFound("productQuantity.equals=" + DEFAULT_PRODUCT_QUANTITY);

        // Get all the invoiceLineItemList where productQuantity equals to UPDATED_PRODUCT_QUANTITY
        defaultInvoiceLineItemShouldNotBeFound("productQuantity.equals=" + UPDATED_PRODUCT_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productQuantity in DEFAULT_PRODUCT_QUANTITY or UPDATED_PRODUCT_QUANTITY
        defaultInvoiceLineItemShouldBeFound("productQuantity.in=" + DEFAULT_PRODUCT_QUANTITY + "," + UPDATED_PRODUCT_QUANTITY);

        // Get all the invoiceLineItemList where productQuantity equals to UPDATED_PRODUCT_QUANTITY
        defaultInvoiceLineItemShouldNotBeFound("productQuantity.in=" + UPDATED_PRODUCT_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productQuantity is not null
        defaultInvoiceLineItemShouldBeFound("productQuantity.specified=true");

        // Get all the invoiceLineItemList where productQuantity is null
        defaultInvoiceLineItemShouldNotBeFound("productQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productQuantity greater than or equals to DEFAULT_PRODUCT_QUANTITY
        defaultInvoiceLineItemShouldBeFound("productQuantity.greaterOrEqualThan=" + DEFAULT_PRODUCT_QUANTITY);

        // Get all the invoiceLineItemList where productQuantity greater than or equals to UPDATED_PRODUCT_QUANTITY
        defaultInvoiceLineItemShouldNotBeFound("productQuantity.greaterOrEqualThan=" + UPDATED_PRODUCT_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productQuantity less than or equals to DEFAULT_PRODUCT_QUANTITY
        defaultInvoiceLineItemShouldNotBeFound("productQuantity.lessThan=" + DEFAULT_PRODUCT_QUANTITY);

        // Get all the invoiceLineItemList where productQuantity less than or equals to UPDATED_PRODUCT_QUANTITY
        defaultInvoiceLineItemShouldBeFound("productQuantity.lessThan=" + UPDATED_PRODUCT_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductCostIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productCost equals to DEFAULT_PRODUCT_COST
        defaultInvoiceLineItemShouldBeFound("productCost.equals=" + DEFAULT_PRODUCT_COST);

        // Get all the invoiceLineItemList where productCost equals to UPDATED_PRODUCT_COST
        defaultInvoiceLineItemShouldNotBeFound("productCost.equals=" + UPDATED_PRODUCT_COST);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductCostIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productCost in DEFAULT_PRODUCT_COST or UPDATED_PRODUCT_COST
        defaultInvoiceLineItemShouldBeFound("productCost.in=" + DEFAULT_PRODUCT_COST + "," + UPDATED_PRODUCT_COST);

        // Get all the invoiceLineItemList where productCost equals to UPDATED_PRODUCT_COST
        defaultInvoiceLineItemShouldNotBeFound("productCost.in=" + UPDATED_PRODUCT_COST);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productCost is not null
        defaultInvoiceLineItemShouldBeFound("productCost.specified=true");

        // Get all the invoiceLineItemList where productCost is null
        defaultInvoiceLineItemShouldNotBeFound("productCost.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productCost greater than or equals to DEFAULT_PRODUCT_COST
        defaultInvoiceLineItemShouldBeFound("productCost.greaterOrEqualThan=" + DEFAULT_PRODUCT_COST);

        // Get all the invoiceLineItemList where productCost greater than or equals to UPDATED_PRODUCT_COST
        defaultInvoiceLineItemShouldNotBeFound("productCost.greaterOrEqualThan=" + UPDATED_PRODUCT_COST);
    }

    @Test
    @Transactional
    public void getAllInvoiceLineItemsByProductCostIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList where productCost less than or equals to DEFAULT_PRODUCT_COST
        defaultInvoiceLineItemShouldNotBeFound("productCost.lessThan=" + DEFAULT_PRODUCT_COST);

        // Get all the invoiceLineItemList where productCost less than or equals to UPDATED_PRODUCT_COST
        defaultInvoiceLineItemShouldBeFound("productCost.lessThan=" + UPDATED_PRODUCT_COST);
    }


    @Test
    @Transactional
    public void getAllInvoiceLineItemsByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        Invoice invoiceNumber = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoiceNumber);
        em.flush();
        invoiceLineItem.setInvoiceNumber(invoiceNumber);
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);
        Long invoiceNumberId = invoiceNumber.getId();

        // Get all the invoiceLineItemList where invoiceNumber equals to invoiceNumberId
        defaultInvoiceLineItemShouldBeFound("invoiceNumberId.equals=" + invoiceNumberId);

        // Get all the invoiceLineItemList where invoiceNumber equals to invoiceNumberId + 1
        defaultInvoiceLineItemShouldNotBeFound("invoiceNumberId.equals=" + (invoiceNumberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvoiceLineItemShouldBeFound(String filter) throws Exception {
        restInvoiceLineItemMockMvc.perform(get("/api/invoice-line-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceLineItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceLineItemNumber").value(hasItem(DEFAULT_INVOICE_LINE_ITEM_NUMBER)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].productQuantity").value(hasItem(DEFAULT_PRODUCT_QUANTITY)))
            .andExpect(jsonPath("$.[*].productCost").value(hasItem(DEFAULT_PRODUCT_COST)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInvoiceLineItemShouldNotBeFound(String filter) throws Exception {
        restInvoiceLineItemMockMvc.perform(get("/api/invoice-line-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInvoiceLineItem() throws Exception {
        // Get the invoiceLineItem
        restInvoiceLineItemMockMvc.perform(get("/api/invoice-line-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceLineItem() throws Exception {
        // Initialize the database
        invoiceLineItemService.save(invoiceLineItem);

        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();

        // Update the invoiceLineItem
        InvoiceLineItem updatedInvoiceLineItem = invoiceLineItemRepository.findById(invoiceLineItem.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceLineItem are not directly saved in db
        em.detach(updatedInvoiceLineItem);
        updatedInvoiceLineItem
            .invoiceLineItemNumber(UPDATED_INVOICE_LINE_ITEM_NUMBER)
            .productName(UPDATED_PRODUCT_NAME)
            .productQuantity(UPDATED_PRODUCT_QUANTITY)
            .productCost(UPDATED_PRODUCT_COST);

        restInvoiceLineItemMockMvc.perform(put("/api/invoice-line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceLineItem)))
            .andExpect(status().isOk());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceLineItem testInvoiceLineItem = invoiceLineItemList.get(invoiceLineItemList.size() - 1);
        assertThat(testInvoiceLineItem.getInvoiceLineItemNumber()).isEqualTo(UPDATED_INVOICE_LINE_ITEM_NUMBER);
        assertThat(testInvoiceLineItem.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testInvoiceLineItem.getProductQuantity()).isEqualTo(UPDATED_PRODUCT_QUANTITY);
        assertThat(testInvoiceLineItem.getProductCost()).isEqualTo(UPDATED_PRODUCT_COST);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();

        // Create the InvoiceLineItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceLineItemMockMvc.perform(put("/api/invoice-line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoiceLineItem() throws Exception {
        // Initialize the database
        invoiceLineItemService.save(invoiceLineItem);

        int databaseSizeBeforeDelete = invoiceLineItemRepository.findAll().size();

        // Get the invoiceLineItem
        restInvoiceLineItemMockMvc.perform(delete("/api/invoice-line-items/{id}", invoiceLineItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceLineItem.class);
        InvoiceLineItem invoiceLineItem1 = new InvoiceLineItem();
        invoiceLineItem1.setId(1L);
        InvoiceLineItem invoiceLineItem2 = new InvoiceLineItem();
        invoiceLineItem2.setId(invoiceLineItem1.getId());
        assertThat(invoiceLineItem1).isEqualTo(invoiceLineItem2);
        invoiceLineItem2.setId(2L);
        assertThat(invoiceLineItem1).isNotEqualTo(invoiceLineItem2);
        invoiceLineItem1.setId(null);
        assertThat(invoiceLineItem1).isNotEqualTo(invoiceLineItem2);
    }
}
