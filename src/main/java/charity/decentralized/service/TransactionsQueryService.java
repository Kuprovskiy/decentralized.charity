package charity.decentralized.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.JoinType;

import charity.decentralized.repository.ProjectRepository;
import charity.decentralized.web.rest.errors.ProjectNotFoundException;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import charity.decentralized.domain.Transactions;
import charity.decentralized.domain.*; // for static metamodels
import charity.decentralized.repository.TransactionsRepository;
import charity.decentralized.service.dto.TransactionsCriteria;
import charity.decentralized.service.dto.TransactionsDTO;
import charity.decentralized.service.mapper.TransactionsMapper;

/**
 * Service for executing complex queries for {@link Transactions} entities in the database.
 * The main input is a {@link TransactionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionsDTO} or a {@link Page} of {@link TransactionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionsQueryService extends QueryService<Transactions> {

    private final Logger log = LoggerFactory.getLogger(TransactionsQueryService.class);

    private final TransactionsRepository transactionsRepository;

    private final ProjectRepository projectRepository;

    private final TransactionsMapper transactionsMapper;

    public TransactionsQueryService(TransactionsRepository transactionsRepository,
                                    TransactionsMapper transactionsMapper,
                                    ProjectRepository projectRepository) {
        this.transactionsRepository = transactionsRepository;
        this.transactionsMapper = transactionsMapper;
        this.projectRepository = projectRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionsDTO> findByCriteria(TransactionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Transactions> specification = createSpecification(criteria);
        return transactionsMapper.toDto(transactionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransactionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionsDTO> findByCriteria(TransactionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transactions> specification = createSpecification(criteria);
        return transactionsRepository.findAll(specification, page)
            .map(transactionsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<TransactionsDTO> findByCriteriaPerProject(Pageable page, Long projectId) {
        log.debug("find by criteria : {}, page: {}", page);

//        final Specification<Transactions> specification = createSpecification(criteria);

        Project project = projectRepository.findById(projectId).orElseThrow(
            () -> new ProjectNotFoundException()
        );

        return transactionsRepository.findAllByProject(page, project)
            .map(transactionsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Transactions> specification = createSpecification(criteria);
        return transactionsRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<Transactions> createSpecification(TransactionsCriteria criteria) {
        Specification<Transactions> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Transactions_.id));
            }
            if (criteria.getTxid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTxid(), Transactions_.txid));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Transactions_.amount));
            }
            if (criteria.getTransactionType() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionType(), Transactions_.transactionType));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Transactions_.createdDate));
            }
            if (criteria.getTransactionStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionStatus(), Transactions_.transactionStatus));
            }
            if (criteria.getBlockHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBlockHeight(), Transactions_.blockHeight));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), Transactions_.key));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Transactions_.note));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProjectId(),
                    root -> root.join(Transactions_.project, JoinType.LEFT).get(Project_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Transactions_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
