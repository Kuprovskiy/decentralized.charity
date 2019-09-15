package charity.decentralized.service;

import charity.decentralized.domain.Transactions;
import charity.decentralized.service.dto.BloqlyTransactionsDTO;
import charity.decentralized.service.dto.TransactionsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link charity.decentralized.domain.Transactions}.
 */
public interface TransactionsService {

    /**
     * Save a transactions.
     *
     * @param transactionsDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionsDTO save(TransactionsDTO transactionsDTO);

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" transactions.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionsDTO> findOne(Long id);

    String saveToBlockly(BloqlyTransactionsDTO bloqlyTransactionsDTO);

    Page<TransactionsDTO> findAllDonateByProject(Pageable pageable, Long id);

    Page<TransactionsDTO> findAllSupplychainTransactionsByProject(Pageable pageable, Long id);
}
