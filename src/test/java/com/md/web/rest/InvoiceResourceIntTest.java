package com.md.web.rest;

import com.md.MdApp;

import com.md.domain.Invoice;
import com.md.repository.InvoiceRepository;
import com.md.service.InvoiceService;
import com.md.web.rest.errors.ExceptionTranslator;
import com.md.service.dto.InvoiceCriteria;
import com.md.service.InvoiceQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.md.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InvoiceResource REST controller.
 *
 * @see InvoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MdApp.class)
public class InvoiceResourceIntTest {

    private static final Integer DEFAULT_INVOICE_NUMBER = 100;
    private static final Integer UPDATED_INVOICE_NUMBER = 101;

    private static final LocalDate DEFAULT_INVOICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVOICE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_INVOICE_AMOUNT = 100;
    private static final Integer UPDATED_INVOICE_AMOUNT = 101;

    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceQueryService invoiceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceService, invoiceQueryService);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
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
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .customerName(DEFAULT_CUSTOMER_NAME)
            .invoiceAmount(DEFAULT_INVOICE_AMOUNT);
        return invoice;
    }

    @Before
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testInvoice.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testInvoice.getInvoiceAmount()).isEqualTo(DEFAULT_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    public void createInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice with an existing ID
        invoice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInvoiceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvoiceNumber(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvoiceDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvoiceDate(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setCustomerName(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvoiceAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvoiceAmount(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())))
            .andExpect(jsonPath("$.[*].invoiceAmount").value(hasItem(DEFAULT_INVOICE_AMOUNT)));
    }
    
    @Test
    @Transactional
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME.toString()))
            .andExpect(jsonPath("$.invoiceAmount").value(DEFAULT_INVOICE_AMOUNT));
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber equals to DEFAULT_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER);

        // Get all the invoiceList where invoiceNumber equals to UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber in DEFAULT_INVOICE_NUMBER or UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER);

        // Get all the invoiceList where invoiceNumber equals to UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoiceNumber.in=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber is not null
        defaultInvoiceShouldBeFound("invoiceNumber.specified=true");

        // Get all the invoiceList where invoiceNumber is null
        defaultInvoiceShouldNotBeFound("invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber greater than or equals to DEFAULT_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoiceNumber.greaterOrEqualThan=" + DEFAULT_INVOICE_NUMBER);

        // Get all the invoiceList where invoiceNumber greater than or equals to UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoiceNumber.greaterOrEqualThan=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber less than or equals to DEFAULT_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoiceNumber.lessThan=" + DEFAULT_INVOICE_NUMBER);

        // Get all the invoiceList where invoiceNumber less than or equals to UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoiceNumber.lessThan=" + UPDATED_INVOICE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllInvoicesByInvoiceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate equals to DEFAULT_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.equals=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate equals to UPDATED_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.equals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceDateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate in DEFAULT_INVOICE_DATE or UPDATED_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.in=" + DEFAULT_INVOICE_DATE + "," + UPDATED_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate equals to UPDATED_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.in=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate is not null
        defaultInvoiceShouldBeFound("invoiceDate.specified=true");

        // Get all the invoiceList where invoiceDate is null
        defaultInvoiceShouldNotBeFound("invoiceDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate greater than or equals to DEFAULT_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.greaterOrEqualThan=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate greater than or equals to UPDATED_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.greaterOrEqualThan=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate less than or equals to DEFAULT_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.lessThan=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate less than or equals to UPDATED_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.lessThan=" + UPDATED_INVOICE_DATE);
    }


    @Test
    @Transactional
    public void getAllInvoicesByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultInvoiceShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the invoiceList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultInvoiceShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultInvoiceShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the invoiceList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultInvoiceShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where customerName is not null
        defaultInvoiceShouldBeFound("customerName.specified=true");

        // Get all the invoiceList where customerName is null
        defaultInvoiceShouldNotBeFound("customerName.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount equals to DEFAULT_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.equals=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount equals to UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.equals=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount in DEFAULT_INVOICE_AMOUNT or UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.in=" + DEFAULT_INVOICE_AMOUNT + "," + UPDATED_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount equals to UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.in=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount is not null
        defaultInvoiceShouldBeFound("invoiceAmount.specified=true");

        // Get all the invoiceList where invoiceAmount is null
        defaultInvoiceShouldNotBeFound("invoiceAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount greater than or equals to DEFAULT_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.greaterOrEqualThan=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount greater than or equals to UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.greaterOrEqualThan=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount less than or equals to DEFAULT_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.lessThan=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount less than or equals to UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.lessThan=" + UPDATED_INVOICE_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())))
            .andExpect(jsonPath("$.[*].invoiceAmount").value(hasItem(DEFAULT_INVOICE_AMOUNT)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceService.save(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .customerName(UPDATED_CUSTOMER_NAME)
            .invoiceAmount(UPDATED_INVOICE_AMOUNT);

        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoice)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testInvoice.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testInvoice.getInvoiceAmount()).isEqualTo(UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Create the Invoice

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceService.save(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Get the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        Invoice invoice2 = new Invoice();
        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);
        invoice2.setId(2L);
        assertThat(invoice1).isNotEqualTo(invoice2);
        invoice1.setId(null);
        assertThat(invoice1).isNotEqualTo(invoice2);
    }
}
