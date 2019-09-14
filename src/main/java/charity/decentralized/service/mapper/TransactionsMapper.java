package charity.decentralized.service.mapper;

import charity.decentralized.domain.*;
import charity.decentralized.service.dto.TransactionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transactions} and its DTO {@link TransactionsDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class, UserMapper.class})
public interface TransactionsMapper extends EntityMapper<TransactionsDTO, Transactions> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    TransactionsDTO toDto(Transactions transactions);

    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "userId", target = "user")
    Transactions toEntity(TransactionsDTO transactionsDTO);

    default Transactions fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transactions transactions = new Transactions();
        transactions.setId(id);
        return transactions;
    }
}
