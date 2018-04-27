package eu.futuretrust.gtsl.ethereum.deployment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Properties;

public class Deployment {

  public static void main(String[] args) {
    if (args.length == 2) {
      BigInteger quorum;
      try {
        quorum = new BigInteger(args[0]);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        return;
      }

      Properties prop = new Properties();
      InputStream input = Deployment.class.getClassLoader().getResourceAsStream("ethereum.properties");
      OutputStream output = null;

      try {
        prop.load(input);

        String endpoint = prop.getProperty("ethereum.endpoint");
        String keystorePath = prop.getProperty("ethereum.keystorePath");
        String password = prop.getProperty("ethereum.password");

        DeploymentHandler deploymentHandler = new DeploymentHandler(endpoint, keystorePath, password);

        String consortiumAddress = deploymentHandler
            .deployConsortiumContract(quorum, args[1]);
        String rulesPropertiesAddress = deploymentHandler.deployRulesPropertiesContract(consortiumAddress);
        String tslStoreAddress = deploymentHandler.deployTslStoreContract(consortiumAddress);

        output = new FileOutputStream("outputs/contract.properties");

        prop = new Properties();
        prop.setProperty("contract.consortium", consortiumAddress);
        prop.setProperty("contract.rulesProperties", rulesPropertiesAddress);
        prop.setProperty("contract.tslStore", tslStoreAddress);

        prop.store(output, null);
      } catch (Exception e) {
        System.out.println("Deployment failed !");
        e.printStackTrace();
      } finally {
        try {
          if (output != null) {
            output.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } else {
      System.out.println("usage: java -jar <jar-file> <quorum_in_percent> <tsl_identifier>");
    }
  }

}