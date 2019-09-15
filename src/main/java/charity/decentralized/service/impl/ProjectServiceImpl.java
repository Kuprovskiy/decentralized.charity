package charity.decentralized.service.impl;

import charity.decentralized.domain.enumeration.TransactionType;
import charity.decentralized.service.ProjectService;
import charity.decentralized.domain.Project;
import charity.decentralized.repository.ProjectRepository;
import charity.decentralized.service.TransactionsService;
import charity.decentralized.service.dto.AddressDTO;
import charity.decentralized.service.dto.BloqlyTransactionsDTO;
import charity.decentralized.service.dto.PayDTO;
import charity.decentralized.service.dto.ProjectDTO;
import charity.decentralized.service.mapper.ProjectMapper;
import charity.decentralized.web.rest.errors.ProjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Project}.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final TransactionsService transactionsService;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              ProjectMapper projectMapper,
                              TransactionsService transactionsService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.transactionsService = transactionsService;
    }

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ProjectDTO save(ProjectDTO projectDTO) {
        log.debug("Request to save Project : {}", projectDTO);
        Project project = projectMapper.toEntity(projectDTO);
        AddressDTO addressDTO = createAccountCredentials(project.getId().toString());
        project.setPrivateKey(addressDTO.getKey());
        project.setAddress(addressDTO.getAddress());
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectRepository.findAll(pageable)
            .map(projectMapper::toDto);
    }


    /**
     * Get one project by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectDTO> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findById(id)
            .map(projectMapper::toDto);
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }

    // create new full eth address
    private AddressDTO createAccountCredentials(String password) {
        AddressDTO addressDTO = new AddressDTO();

        try {
            ECKeyPair keyPair = Keys.createEcKeyPair();
            WalletFile wallet = Wallet.createStandard(password, keyPair);

            addressDTO.setKey(keyPair.getPrivateKey().toString(16));
            addressDTO.setAddress(wallet.getAddress());

            System.out.println("Private key: " + keyPair.getPrivateKey().toString(16));
            System.out.println("Account: " + wallet.getAddress());
        } catch (Exception e) {
            log.error("createAccountCredentials {}", e.getMessage());
        }
        return addressDTO;
    }

    @Override
    public void pay(PayDTO payDTO, Long id) {
        BloqlyTransactionsDTO bloqlyTransactionsDTO = new BloqlyTransactionsDTO();
        bloqlyTransactionsDTO.setAmount(payDTO.getAmount());
        bloqlyTransactionsDTO.setProjectId(id);
        transactionsService.saveToBlockly(bloqlyTransactionsDTO, TransactionType.SUPPLY_CHAIN);
    }
}
