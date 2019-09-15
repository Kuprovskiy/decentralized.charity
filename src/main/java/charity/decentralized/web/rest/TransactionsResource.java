package charity.decentralized.web.rest;

import charity.decentralized.domain.Transactions;
import charity.decentralized.domain.enumeration.TransactionType;
import charity.decentralized.service.TransactionsService;
import charity.decentralized.service.dto.BloqlyTransactionsDTO;
import charity.decentralized.web.rest.errors.BadRequestAlertException;
import charity.decentralized.service.dto.TransactionsDTO;
import charity.decentralized.service.dto.TransactionsCriteria;
import charity.decentralized.service.TransactionsQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link charity.decentralized.domain.Transactions}.
 */
@RestController
@RequestMapping("/api")
public class TransactionsResource {

    private final Logger log = LoggerFactory.getLogger(TransactionsResource.class);

    private static final String ENTITY_NAME = "transactions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionsService transactionsService;

    private final TransactionsQueryService transactionsQueryService;

    public TransactionsResource(TransactionsService transactionsService, TransactionsQueryService transactionsQueryService) {
        this.transactionsService = transactionsService;
        this.transactionsQueryService = transactionsQueryService;
    }

    /**
     * {@code POST  /transactions} : Create a new transactions.
     *
     * @param transactionsDTO the transactionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionsDTO, or with status {@code 400 (Bad Request)} if the transactions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions")
    public ResponseEntity<TransactionsDTO> createTransactions(@Valid @RequestBody TransactionsDTO transactionsDTO) throws URISyntaxException {
        log.debug("REST request to save Transactions : {}", transactionsDTO);
        if (transactionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionsDTO result = transactionsService.save(transactionsDTO);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transactions} : Updates an existing transactions.
     *
     * @param transactionsDTO the transactionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionsDTO,
     * or with status {@code 400 (Bad Request)} if the transactionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transactions")
    public ResponseEntity<TransactionsDTO> updateTransactions(@Valid @RequestBody TransactionsDTO transactionsDTO) throws URISyntaxException {
        log.debug("REST request to update Transactions : {}", transactionsDTO);
        if (transactionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TransactionsDTO result = transactionsService.save(transactionsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /transactions} : get all the transactions.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionsDTO>> getAllTransactions(TransactionsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Transactions by criteria: {}", criteria);
        Page<TransactionsDTO> page = transactionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /transactions/count} : count all the transactions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/transactions/count")
    public ResponseEntity<Long> countTransactions(TransactionsCriteria criteria) {
        log.debug("REST request to count Transactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" transactions.
     *
     * @param id the id of the transactionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionsDTO> getTransactions(@PathVariable Long id) {
        log.debug("REST request to get Transactions : {}", id);
        Optional<TransactionsDTO> transactionsDTO = transactionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionsDTO);
    }

    @GetMapping("/project/{projectId}/transactions")
    public ResponseEntity<List<TransactionsDTO>> getAllTransactionsPerProject(Pageable pageable, @PathVariable Long projectId) {
        log.debug("REST request to get Transactions by criteria: {}");
        Page<TransactionsDTO> page = transactionsQueryService.findByCriteriaPerProject(pageable, projectId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/bloqly/transactions")
    public ResponseEntity createTransactions(@Valid @RequestBody BloqlyTransactionsDTO bloqlyTransactionsDTO) throws URISyntaxException {
        log.debug("REST request to save Transactions : {}", bloqlyTransactionsDTO);

        String txHash = transactionsService.saveToBlockly(bloqlyTransactionsDTO, TransactionType.SUPPLY_CHAIN);
        return ResponseEntity.ok().body(txHash);
    }

    @GetMapping("/transactions/{id}/donate")
    public ResponseEntity<List<TransactionsDTO>> getTransactionLocations(Pageable pageable, @PathVariable Long id) {
        log.debug("REST request to get Transactions : {}", id);

        Page<TransactionsDTO> page = transactionsService.findAllDonateByProject(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/transactions/{id}/supplychain")
    public ResponseEntity<List<TransactionsDTO>> getTransactionSupplychain(Pageable pageable, @PathVariable Long id) {
        log.debug("REST request to get Transactions : {}", id);

        Page<TransactionsDTO> page = transactionsService.findAllSupplychainTransactionsByProject(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
