/**
 * © Nowina Solutions, 2015-2016
 *
 * Concédée sous licence EUPL, version 1.1 ou – dès leur approbation par la Commission européenne - versions ultérieures de l’EUPL (la «Licence»).
 * Vous ne pouvez utiliser la présente œuvre que conformément à la Licence.
 * Vous pouvez obtenir une copie de la Licence à l’adresse suivante:
 *
 * http://ec.europa.eu/idabc/eupl5
 *
 * Sauf obligation légale ou contractuelle écrite, le logiciel distribué sous la Licence est distribué «en l’état»,
 * SANS GARANTIES OU CONDITIONS QUELLES QU’ELLES SOIENT, expresses ou implicites.
 * Consultez la Licence pour les autorisations et les restrictions linguistiques spécifiques relevant de la Licence.
 */
package eu.futuretrust.gtsl.admin.nexu;

/**
 * Base class of requests of NexU. 
 *
 * @author Jean Lepropre (jean.lepropre@nowina.lu)
 */
public class NexuRequest {

  private String userLocale;
  private String externalId;
  private String requestSeal;
  private String nonce;

  public NexuRequest() {
    super();
  }

  public String getUserLocale() {
    return userLocale;
  }

  public void setUserLocale(String userLocale) {
    this.userLocale = userLocale;
  }

  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public String getRequestSeal() {
    return requestSeal;
  }

  public void setRequestSeal(String requestSeal) {
    this.requestSeal = requestSeal;
  }

  public String getNonce() {
    return nonce;
  }

  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

}