package charity.decentralized.repository;
import charity.decentralized.domain.Project;
import charity.decentralized.domain.Transactions;
import charity.decentralized.domain.enumeration.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Transactions entity.
 */
@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long>, JpaSpecificationExecutor<Transactions> {

    @Query("select transactions from Transactions transactions where transactions.user.login = ?#{principal.username}")
    List<Transactions> findByUserIsCurrentUser();

    Page<Transactions> findAllByProject(Pageable pageable, Project project);

    Page<Transactions> findAllByProjectAndTransactionType(Pageable pageable, Project project, TransactionType transactionType);
}
