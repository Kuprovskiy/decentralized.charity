package charity.decentralized.web.rest;

import charity.decentralized.DecentralizedcharityApp;
import charity.decentralized.domain.Transactions;
import charity.decentralized.domain.Project;
import charity.decentralized.domain.User;
import charity.decentralized.repository.TransactionsRepository;
import charity.decentralized.service.TransactionsService;
import charity.decentralized.service.dto.TransactionsDTO;
import charity.decentralized.service.mapper.TransactionsMapper;
import charity.decentralized.web.rest.errors.ExceptionTranslator;
import charity.decentralized.service.dto.TransactionsCriteria;
import charity.decentralized.service.TransactionsQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static charity.decentralized.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import charity.decentralized.domain.enumeration.TransactionType;
/**
 * Integration tests for the {@link TransactionsResource} REST controller.
 */
@SpringBootTest(classes = DecentralizedcharityApp.class)
public class TransactionsResourceIT {

    private static final String DEFAULT_TXID = "AAAAAAAAAA";
    private static final String UPDATED_TXID = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.DONATE;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.WITHDRAW;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CREATED_DATE = Instant.ofEpochMilli(-1L);

    private static final Boolean DEFAULT_TRANSACTION_STATUS = false;
    private static final Boolean UPDATED_TRANSACTION_STATUS = true;

    private static final Long DEFAULT_BLOCK_HEIGHT = 1L;
    private static final Long UPDATED_BLOCK_HEIGHT = 2L;
    private static final Long SMALLER_BLOCK_HEIGHT = 1L - 1L;

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private TransactionsMapper transactionsMapper;

    @Autowired
    private TransactionsService transactionsService;

    @Autowired
    private TransactionsQueryService transactionsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTransactionsMockMvc;

    private Transactions transactions;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionsResource transactionsResource = new TransactionsResource(transactionsService, transactionsQueryService);
        this.restTransactionsMockMvc = MockMvcBuilders.standaloneSetup(transactionsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transactions createEntity(EntityManager em) {
        Transactions transactions = new Transactions()
            .txid(DEFAULT_TXID)
            .amount(DEFAULT_AMOUNT)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .transactionStatus(DEFAULT_TRANSACTION_STATUS)
            .blockHeight(DEFAULT_BLOCK_HEIGHT)
            .key(DEFAULT_KEY)
            .note(DEFAULT_NOTE);
        return transactions;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transactions createUpdatedEntity(EntityManager em) {
        Transactions transactions = new Transactions()
            .txid(UPDATED_TXID)
            .amount(UPDATED_AMOUNT)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .transactionStatus(UPDATED_TRANSACTION_STATUS)
            .blockHeight(UPDATED_BLOCK_HEIGHT)
            .key(UPDATED_KEY)
            .note(UPDATED_NOTE);
        return transactions;
    }

    @BeforeEach
    public void initTest() {
        transactions = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactions() throws Exception {
        int databaseSizeBeforeCreate = transactionsRepository.findAll().size();

        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);
        restTransactionsMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
            .andExpect(status().isCreated());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeCreate + 1);
        Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
        assertThat(testTransactions.getTxid()).isEqualTo(DEFAULT_TXID);
        assertThat(testTransactions.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransactions.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testTransactions.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTransactions.isTransactionStatus()).isEqualTo(DEFAULT_TRANSACTION_STATUS);
        assertThat(testTransactions.getBlockHeight()).isEqualTo(DEFAULT_BLOCK_HEIGHT);
        assertThat(testTransactions.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testTransactions.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createTransactionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionsRepository.findAll().size();

        // Create the Transactions with an existing ID
        transactions.setId(1L);
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionsMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTxidIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionsRepository.findAll().size();
        // set the field null
        transactions.setTxid(null);

        // Create the Transactions, which fails.
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        restTransactionsMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
            .andExpect(status().isBadRequest());

        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionsRepository.findAll().size();
        // set the field null
        transactions.setCreatedDate(null);

        // Create the Transactions, which fails.
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        restTransactionsMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
            .andExpect(status().isBadRequest());

        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList
        restTransactionsMockMvc.perform(get("/api/transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].txid").value(hasItem(DEFAULT_TXID.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionStatus").value(hasItem(DEFAULT_TRANSACTION_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].blockHeight").value(hasItem(DEFAULT_BLOCK_HEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }
    
    @Test
    @Transactional
    public void getTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get the transactions
        restTransactionsMockMvc.perform(get("/api/transactions/{id}", transactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactions.getId().intValue()))
            .andExpect(jsonPath("$.txid").value(DEFAULT_TXID.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.transactionStatus").value(DEFAULT_TRANSACTION_STATUS.booleanValue()))
            .andExpect(jsonPath("$.blockHeight").value(DEFAULT_BLOCK_HEIGHT.intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getAllTransactionsByTxidIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where txid equals to DEFAULT_TXID
        defaultTransactionsShouldBeFound("txid.equals=" + DEFAULT_TXID);

        // Get all the transactionsList where txid equals to UPDATED_TXID
        defaultTransactionsShouldNotBeFound("txid.equals=" + UPDATED_TXID);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTxidIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where txid in DEFAULT_TXID or UPDATED_TXID
        defaultTransactionsShouldBeFound("txid.in=" + DEFAULT_TXID + "," + UPDATED_TXID);

        // Get all the transactionsList where txid equals to UPDATED_TXID
        defaultTransactionsShouldNotBeFound("txid.in=" + UPDATED_TXID);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTxidIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where txid is not null
        defaultTransactionsShouldBeFound("txid.specified=true");

        // Get all the transactionsList where txid is null
        defaultTransactionsShouldNotBeFound("txid.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount equals to DEFAULT_AMOUNT
        defaultTransactionsShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the transactionsList where amount equals to UPDATED_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultTransactionsShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the transactionsList where amount equals to UPDATED_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount is not null
        defaultTransactionsShouldBeFound("amount.specified=true");

        // Get all the transactionsList where amount is null
        defaultTransactionsShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultTransactionsShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the transactionsList where amount is greater than or equal to UPDATED_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount is less than or equal to DEFAULT_AMOUNT
        defaultTransactionsShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the transactionsList where amount is less than or equal to SMALLER_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount is less than DEFAULT_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the transactionsList where amount is less than UPDATED_AMOUNT
        defaultTransactionsShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount is greater than DEFAULT_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the transactionsList where amount is greater than SMALLER_AMOUNT
        defaultTransactionsShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllTransactionsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where transactionType equals to DEFAULT_TRANSACTION_TYPE
        defaultTransactionsShouldBeFound("transactionType.equals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionsList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionsShouldNotBeFound("transactionType.equals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where transactionType in DEFAULT_TRANSACTION_TYPE or UPDATED_TRANSACTION_TYPE
        defaultTransactionsShouldBeFound("transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE);

        // Get all the transactionsList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionsShouldNotBeFound("transactionType.in=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where transactionType is not null
        defaultTransactionsShouldBeFound("transactionType.specified=true");

        // Get all the transactionsList where transactionType is null
        defaultTransactionsShouldNotBeFound("transactionType.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where createdDate equals to DEFAULT_CREATED_DATE
        defaultTransactionsShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the transactionsList where createdDate equals to UPDATED_CREATED_DATE
        defaultTransactionsShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultTransactionsShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the transactionsList where createdDate equals to UPDATED_CREATED_DATE
        defaultTransactionsShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where createdDate is not null
        defaultTransactionsShouldBeFound("createdDate.specified=true");

        // Get all the transactionsList where createdDate is null
        defaultTransactionsShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactionStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where transactionStatus equals to DEFAULT_TRANSACTION_STATUS
        defaultTransactionsShouldBeFound("transactionStatus.equals=" + DEFAULT_TRANSACTION_STATUS);

        // Get all the transactionsList where transactionStatus equals to UPDATED_TRANSACTION_STATUS
        defaultTransactionsShouldNotBeFound("transactionStatus.equals=" + UPDATED_TRANSACTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactionStatusIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where transactionStatus in DEFAULT_TRANSACTION_STATUS or UPDATED_TRANSACTION_STATUS
        defaultTransactionsShouldBeFound("transactionStatus.in=" + DEFAULT_TRANSACTION_STATUS + "," + UPDATED_TRANSACTION_STATUS);

        // Get all the transactionsList where transactionStatus equals to UPDATED_TRANSACTION_STATUS
        defaultTransactionsShouldNotBeFound("transactionStatus.in=" + UPDATED_TRANSACTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactionStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where transactionStatus is not null
        defaultTransactionsShouldBeFound("transactionStatus.specified=true");

        // Get all the transactionsList where transactionStatus is null
        defaultTransactionsShouldNotBeFound("transactionStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByBlockHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where blockHeight equals to DEFAULT_BLOCK_HEIGHT
        defaultTransactionsShouldBeFound("blockHeight.equals=" + DEFAULT_BLOCK_HEIGHT);

        // Get all the transactionsList where blockHeight equals to UPDATED_BLOCK_HEIGHT
        defaultTransactionsShouldNotBeFound("blockHeight.equals=" + UPDATED_BLOCK_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByBlockHeightIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where blockHeight in DEFAULT_BLOCK_HEIGHT or UPDATED_BLOCK_HEIGHT
        defaultTransactionsShouldBeFound("blockHeight.in=" + DEFAULT_BLOCK_HEIGHT + "," + UPDATED_BLOCK_HEIGHT);

        // Get all the transactionsList where blockHeight equals to UPDATED_BLOCK_HEIGHT
        defaultTransactionsShouldNotBeFound("blockHeight.in=" + UPDATED_BLOCK_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByBlockHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where blockHeight is not null
        defaultTransactionsShouldBeFound("blockHeight.specified=true");

        // Get all the transactionsList where blockHeight is null
        defaultTransactionsShouldNotBeFound("blockHeight.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByBlockHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where blockHeight is greater than or equal to DEFAULT_BLOCK_HEIGHT
        defaultTransactionsShouldBeFound("blockHeight.greaterThanOrEqual=" + DEFAULT_BLOCK_HEIGHT);

        // Get all the transactionsList where blockHeight is greater than or equal to UPDATED_BLOCK_HEIGHT
        defaultTransactionsShouldNotBeFound("blockHeight.greaterThanOrEqual=" + UPDATED_BLOCK_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByBlockHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where blockHeight is less than or equal to DEFAULT_BLOCK_HEIGHT
        defaultTransactionsShouldBeFound("blockHeight.lessThanOrEqual=" + DEFAULT_BLOCK_HEIGHT);

        // Get all the transactionsList where blockHeight is less than or equal to SMALLER_BLOCK_HEIGHT
        defaultTransactionsShouldNotBeFound("blockHeight.lessThanOrEqual=" + SMALLER_BLOCK_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByBlockHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where blockHeight is less than DEFAULT_BLOCK_HEIGHT
        defaultTransactionsShouldNotBeFound("blockHeight.lessThan=" + DEFAULT_BLOCK_HEIGHT);

        // Get all the transactionsList where blockHeight is less than UPDATED_BLOCK_HEIGHT
        defaultTransactionsShouldBeFound("blockHeight.lessThan=" + UPDATED_BLOCK_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByBlockHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where blockHeight is greater than DEFAULT_BLOCK_HEIGHT
        defaultTransactionsShouldNotBeFound("blockHeight.greaterThan=" + DEFAULT_BLOCK_HEIGHT);

        // Get all the transactionsList where blockHeight is greater than SMALLER_BLOCK_HEIGHT
        defaultTransactionsShouldBeFound("blockHeight.greaterThan=" + SMALLER_BLOCK_HEIGHT);
    }


    @Test
    @Transactional
    public void getAllTransactionsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where key equals to DEFAULT_KEY
        defaultTransactionsShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the transactionsList where key equals to UPDATED_KEY
        defaultTransactionsShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTransactionsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where key in DEFAULT_KEY or UPDATED_KEY
        defaultTransactionsShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the transactionsList where key equals to UPDATED_KEY
        defaultTransactionsShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTransactionsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where key is not null
        defaultTransactionsShouldBeFound("key.specified=true");

        // Get all the transactionsList where key is null
        defaultTransactionsShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where note equals to DEFAULT_NOTE
        defaultTransactionsShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the transactionsList where note equals to UPDATED_NOTE
        defaultTransactionsShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultTransactionsShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the transactionsList where note equals to UPDATED_NOTE
        defaultTransactionsShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where note is not null
        defaultTransactionsShouldBeFound("note.specified=true");

        // Get all the transactionsList where note is null
        defaultTransactionsShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);
        Project project = ProjectResourceIT.createEntity(em);
        em.persist(project);
        em.flush();
        transactions.setProject(project);
        transactionsRepository.saveAndFlush(transactions);
        Long projectId = project.getId();

        // Get all the transactionsList where project equals to projectId
        defaultTransactionsShouldBeFound("projectId.equals=" + projectId);

        // Get all the transactionsList where project equals to projectId + 1
        defaultTransactionsShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }


    @Test
    @Transactional
    public void getAllTransactionsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        transactions.setUser(user);
        transactionsRepository.saveAndFlush(transactions);
        Long userId = user.getId();

        // Get all the transactionsList where user equals to userId
        defaultTransactionsShouldBeFound("userId.equals=" + userId);

        // Get all the transactionsList where user equals to userId + 1
        defaultTransactionsShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionsShouldBeFound(String filter) throws Exception {
        restTransactionsMockMvc.perform(get("/api/transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].txid").value(hasItem(DEFAULT_TXID)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionStatus").value(hasItem(DEFAULT_TRANSACTION_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].blockHeight").value(hasItem(DEFAULT_BLOCK_HEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restTransactionsMockMvc.perform(get("/api/transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionsShouldNotBeFound(String filter) throws Exception {
        restTransactionsMockMvc.perform(get("/api/transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionsMockMvc.perform(get("/api/transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTransactions() throws Exception {
        // Get the transactions
        restTransactionsMockMvc.perform(get("/api/transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();

        // Update the transactions
        Transactions updatedTransactions = transactionsRepository.findById(transactions.getId()).get();
        // Disconnect from session so that the updates on updatedTransactions are not directly saved in db
        em.detach(updatedTransactions);
        updatedTransactions
            .txid(UPDATED_TXID)
            .amount(UPDATED_AMOUNT)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .transactionStatus(UPDATED_TRANSACTION_STATUS)
            .blockHeight(UPDATED_BLOCK_HEIGHT)
            .key(UPDATED_KEY)
            .note(UPDATED_NOTE);
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(updatedTransactions);

        restTransactionsMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
            .andExpect(status().isOk());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
        Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
        assertThat(testTransactions.getTxid()).isEqualTo(UPDATED_TXID);
        assertThat(testTransactions.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransactions.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testTransactions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTransactions.isTransactionStatus()).isEqualTo(UPDATED_TRANSACTION_STATUS);
        assertThat(testTransactions.getBlockHeight()).isEqualTo(UPDATED_BLOCK_HEIGHT);
        assertThat(testTransactions.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testTransactions.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactions() throws Exception {
        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();

        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionsMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        int databaseSizeBeforeDelete = transactionsRepository.findAll().size();

        // Delete the transactions
        restTransactionsMockMvc.perform(delete("/api/transactions/{id}", transactions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transactions.class);
        Transactions transactions1 = new Transactions();
        transactions1.setId(1L);
        Transactions transactions2 = new Transactions();
        transactions2.setId(transactions1.getId());
        assertThat(transactions1).isEqualTo(transactions2);
        transactions2.setId(2L);
        assertThat(transactions1).isNotEqualTo(transactions2);
        transactions1.setId(null);
        assertThat(transactions1).isNotEqualTo(transactions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionsDTO.class);
        TransactionsDTO transactionsDTO1 = new TransactionsDTO();
        transactionsDTO1.setId(1L);
        TransactionsDTO transactionsDTO2 = new TransactionsDTO();
        assertThat(transactionsDTO1).isNotEqualTo(transactionsDTO2);
        transactionsDTO2.setId(transactionsDTO1.getId());
        assertThat(transactionsDTO1).isEqualTo(transactionsDTO2);
        transactionsDTO2.setId(2L);
        assertThat(transactionsDTO1).isNotEqualTo(transactionsDTO2);
        transactionsDTO1.setId(null);
        assertThat(transactionsDTO1).isNotEqualTo(transactionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionsMapper.fromId(null)).isNull();
    }
}
