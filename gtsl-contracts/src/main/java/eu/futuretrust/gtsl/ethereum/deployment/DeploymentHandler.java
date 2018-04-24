package eu.futuretrust.gtsl.ethereum.deployment;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.futuretrust.gtsl.ethereum.contracts.ConsortiumContract;
import eu.futuretrust.gtsl.ethereum.contracts.RulesPropertiesContract;
import eu.futuretrust.gtsl.ethereum.contracts.TslStoreContract;
import eu.futuretrust.gtsl.ethereum.utils.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

public class DeploymentHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentHandler.class);

  private static final BigInteger GAS_LIMIT = BigInteger.valueOf(3141592L);

  private final Web3j web3j;
  private final Credentials credentials;

  public DeploymentHandler(String endpoint, String keystorePath, String password)
      throws IOException {
    this.web3j = Web3j.build(new HttpService(endpoint));
    this.credentials = retrieveCredentials(loadKeystore(keystorePath), password);
  }

  public String deployConsortiumContract(BigInteger quorum, String tslIdentifier)
      throws Exception {
    System.out.println("Deployment Consortium contract...");
    ConsortiumContract contract = ConsortiumContract
        .deploy(web3j, credentials, Contract.GAS_PRICE, GAS_LIMIT, quorum, StringUtils
            .safeBytes(tslIdentifier, 32))
        .send();
    System.out.println("Consortium contract deployed");
    return contract.getContractAddress();
  }

  public String deployRulesPropertiesContract(String userContract)
      throws Exception {
    System.out.println("Deployment RulesProperties contract...");
    RulesPropertiesContract contract = RulesPropertiesContract
        .deploy(web3j, credentials, Contract.GAS_PRICE, GAS_LIMIT, userContract)
        .send();
    System.out.println("RulesProperties contract deployed");
    return contract.getContractAddress();
  }

  public String deployTslStoreContract(String userContract)
      throws Exception {
    System.out.println("Deployment TslStore contract...");
    TslStoreContract contract = TslStoreContract
        .deploy(web3j, credentials, Contract.GAS_PRICE, GAS_LIMIT, userContract)
        .send();
    System.out.println("TslStore contract deployed");
    return contract.getContractAddress();
  }

  private Credentials retrieveCredentials(InputStream keystore, String password) {
    Credentials credentials;
    try {
      ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
      WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
      credentials = Credentials.create(Wallet.decrypt(password, walletFile));
    } catch (IOException | CipherException e) {
      e.printStackTrace();
      return null;
    }
    return credentials;
  }

  private InputStream loadKeystore(String keystorePath) throws IOException {
    return getClass().getClassLoader().getResourceAsStream(keystorePath);
  }

}
