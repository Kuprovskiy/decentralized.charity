package charity.decentralized.service.impl;

import charity.decentralized.bloqly.BloqlyClient;
import charity.decentralized.bloqly.KeyPair;
import charity.decentralized.bloqly.transaction.SignedTransaction;
import charity.decentralized.bloqly.transaction.Transaction;
import charity.decentralized.domain.Project;
import charity.decentralized.domain.enumeration.TransactionType;
import charity.decentralized.repository.ProjectRepository;
import charity.decentralized.service.TransactionsService;
import charity.decentralized.domain.Transactions;
import charity.decentralized.repository.TransactionsRepository;
import charity.decentralized.service.dto.BloqlyTransactionsDTO;
import charity.decentralized.service.dto.TransactionsDTO;
import charity.decentralized.service.mapper.TransactionsMapper;
import charity.decentralized.web.rest.errors.ProjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Transactions}.
 */
@Service
@Transactional
public class TransactionsServiceImpl implements TransactionsService {

    private final Logger log = LoggerFactory.getLogger(TransactionsServiceImpl.class);

    private final TransactionsRepository transactionsRepository;

    private final ProjectRepository projectRepository;

    private final TransactionsMapper transactionsMapper;

    public TransactionsServiceImpl(TransactionsRepository transactionsRepository,
                                   TransactionsMapper transactionsMapper,
                                   ProjectRepository projectRepository) {
        this.transactionsRepository = transactionsRepository;
        this.transactionsMapper = transactionsMapper;
        this.projectRepository = projectRepository;
    }

    @Override
    public String saveToBlockly(BloqlyTransactionsDTO bloqlyTransactionsDTO) {
        String privateKey = "x9oZ6jrUVEBDP5C0005fPseqPwLshQbb9io7Upg8sNM=";
        String space = "space1";
        String key = "key1";
        BloqlyClient bloqlyClient = new BloqlyClient("http://localhost:8082");
        Optional<SignedTransaction> txOpt = bloqlyClient.getLastTransaction(space, key);
        Long nonce = txOpt.map(tx -> tx.getNonce() + 1).orElse(1L);
        KeyPair keyPair = KeyPair.fromPrivateKeyEncoded(privateKey);
        Transaction tx = new Transaction();

        tx.setSpace(space);
        tx.setKey(key);
        tx.setNonce(nonce);
        tx.setMemo("memo");
        tx.setTimestamp(Instant.now().toEpochMilli());
        tx.setValue(bloqlyTransactionsDTO.toString());

        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        tx.setTags(tags);

        SignedTransaction signedTx = keyPair.signTransaction(tx);

        bloqlyClient.submitTransaction(signedTx);

        return signedTx.getHash();
    }

    /**
     * Save a transactions.
     *
     * @param transactionsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TransactionsDTO save(TransactionsDTO transactionsDTO) {
        log.debug("Request to save Transactions : {}", transactionsDTO);
        Transactions transactions = transactionsMapper.toEntity(transactionsDTO);
        transactions = transactionsRepository.save(transactions);
        return transactionsMapper.toDto(transactions);
    }

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        return transactionsRepository.findAll(pageable)
            .map(transactionsMapper::toDto);
    }


    /**
     * Get one transactions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionsDTO> findOne(Long id) {
        log.debug("Request to get Transactions : {}", id);
        return transactionsRepository.findById(id)
            .map(transactionsMapper::toDto);
    }

    @Override
    public List<Transactions> findAllDonateByProject(Long id) {
        log.debug("Request to get all Transactions");

        Project project = projectRepository.getOne(id);
        if (project != null) {
            throw new ProjectNotFoundException();
        }
        return transactionsRepository.findAllByProjectAndTransactionType(project, TransactionType.DONATE);
    }

    @Override
    public List<Transactions> findAllSupplychainTransactionsByProject(Long id) {
        log.debug("Request to get all Transactions");

        Project project = projectRepository.getOne(id);
        if (project != null) {
            throw new ProjectNotFoundException();
        }
        return transactionsRepository.findAllByProjectAndTransactionType(project, TransactionType.SUPPLY_CHAIN);
    }
}
