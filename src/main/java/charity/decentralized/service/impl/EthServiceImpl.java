package charity.decentralized.service.impl;

import charity.decentralized.repository.ProjectRepository;
import charity.decentralized.service.UserService;
import charity.decentralized.service.dto.AddressDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class EthServiceImpl {

    private final Logger log = LoggerFactory.getLogger(EthServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final UserService userService;

    private static Web3jService service;
    private static Web3j web3j;
    private static Parity parity;

    public EthServiceImpl(TransactionsServiceImpl transactionsServiceImpl,
                          ProjectRepository projectRepository,
                          UserService userService) {

        this.projectRepository = projectRepository;
        this.userService = userService;

        this.service = new HttpService("https://rinkeby.infura.io/v3/4750a8f14c37429687f5229ff94e4e56");
        this.web3j = Web3j.build(this.service);
        this.parity = Parity.build(this.service);
    }

    public BigDecimal balanceOf(String accountAddress) {

        BigDecimal balance = BigDecimal.ZERO;
        try {
            EthGetBalance ethGetBalance=
                this.web3j.ethGetBalance(accountAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

            BigInteger wei = ethGetBalance.getBalance();
            balance = Convert.fromWei(String.valueOf(wei), Convert.Unit.ETHER);
        } catch (Exception e) {
            log.error("Request to mint failed : {}", e);
        }

        return balance;
    }

    public String getTransactionStatus(String txHash) {
        String status = "0";
        try {
            EthGetTransactionReceipt ethGetTransactionReceipt =
                this.web3j.ethGetTransactionReceipt(txHash).sendAsync().get();
            String status1 = ethGetTransactionReceipt.getTransactionReceipt().get().getStatus();

            if (status1.length() > 1) {
                status = status1.substring(2);
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } catch (ExecutionException e) {
            log.error(e.getMessage());
        }

        return status;
    }

    // load private key
    public Credentials getAccountCredentials(String pKey) {
        BigInteger key = new BigInteger(pKey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(key);
        Credentials credentials = Credentials.create(ecKeyPair);
        return credentials;
    }

    // create new full eth address
    public AddressDTO createAccountCredentials(String password) {
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

}
