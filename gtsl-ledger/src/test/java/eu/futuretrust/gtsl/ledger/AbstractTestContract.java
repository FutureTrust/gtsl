package eu.futuretrust.gtsl.ledger;

import eu.futuretrust.gtsl.ethereum.contracts.ConsortiumContract;
import eu.futuretrust.gtsl.ethereum.contracts.RulesPropertiesContract;
import eu.futuretrust.gtsl.ethereum.contracts.TslStoreContract;
import eu.futuretrust.gtsl.ethereum.utils.StringUtils;
import java.math.BigInteger;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;

public abstract class AbstractTestContract {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(3141592L);

  protected static final String EMPTY_STRING = "   ";
  protected static final String INVALID_ADDRESS = "random";

  protected static final String VALID_CODE = "EU";
  protected static final String VALID_CODE_FR = "FR";
  protected static final String INVALID_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

  protected static final String VALID_ADDRESS = "QmYmCM2ttrNscwfqSroXP5yVb5LT58zRhvEvMp6WqBHAAA";
  protected static final String VALID_ADDRESS2 = "QmYmCM2ttrNscwfqSroXP5yVb5LT58zRhvEvMp6WqBHBBB";

  protected static String deployConsortium(Web3j web3j, Credentials credentials) throws Exception {
    ConsortiumContract consortiumContract = ConsortiumContract
        .deploy(web3j, credentials, Contract.GAS_PRICE, GAS_LIMIT, BigInteger.valueOf(70),
            StringUtils.safeBytes(VALID_CODE, 32))
        .send();
    return consortiumContract.getContractAddress();
  }

  protected static String deployTslStore(Web3j web3j, Credentials credentials,
      String consortiumAddress) throws Exception {
    TslStoreContract tslStoreContract = TslStoreContract
        .deploy(web3j, credentials, Contract.GAS_PRICE, GAS_LIMIT, consortiumAddress).send();
    return tslStoreContract.getContractAddress();
  }

  protected static String deployRulesProperties(Web3j web3j, Credentials credentials,
      String consortiumAddress) throws Exception {
    RulesPropertiesContract rulesPropertiesContract = RulesPropertiesContract
        .deploy(web3j, credentials, Contract.GAS_PRICE, GAS_LIMIT, consortiumAddress).send();
    return rulesPropertiesContract.getContractAddress();
  }

}
