/*
 * Copyright (c) 2017 European Commission.
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence").
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl5
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence
 * is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * See the Licence for the specific language governing permissions and limitations under the
 * Licence.
 */

package eu.futuretrust.gtsl.properties.rules;

import java.math.BigInteger;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ConstantProperties {

  @NotEmpty
  private String lotlTerritory;
  @NotNull
  @Min(5)
  private BigInteger version;
  @NotNull
  @Min(0)
  private BigInteger historicalPeriod;
  @NotEmpty
  private String englishSchemeName;
  @NotEmpty
  private String englishLegalNotice;

  @Override
  public String toString() {
    return "ConstantProperties{" +
        "lotlTerritory='" + lotlTerritory + '\'' +
        ", version=" + version +
        ", historicalPeriod=" + historicalPeriod +
        ", englishSchemeName='" + englishSchemeName + '\'' +
        ", englishLegalNotice='" + englishLegalNotice + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConstantProperties that = (ConstantProperties) o;

    if (lotlTerritory != null ? !lotlTerritory.equals(that.lotlTerritory)
        : that.lotlTerritory != null) {
      return false;
    }
    if (version != null ? !version.equals(that.version) : that.version != null) {
      return false;
    }
    if (historicalPeriod != null ? !historicalPeriod.equals(that.historicalPeriod)
        : that.historicalPeriod != null) {
      return false;
    }
    if (englishSchemeName != null ? !englishSchemeName.equals(that.englishSchemeName)
        : that.englishSchemeName != null) {
      return false;
    }
    return englishLegalNotice != null ? englishLegalNotice.equals(that.englishLegalNotice)
        : that.englishLegalNotice == null;
  }

  @Override
  public int hashCode() {
    int result = lotlTerritory != null ? lotlTerritory.hashCode() : 0;
    result = 31 * result + (version != null ? version.hashCode() : 0);
    result = 31 * result + (historicalPeriod != null ? historicalPeriod.hashCode() : 0);
    result = 31 * result + (englishSchemeName != null ? englishSchemeName.hashCode() : 0);
    result = 31 * result + (englishLegalNotice != null ? englishLegalNotice.hashCode() : 0);
    return result;
  }

  public String getLotlTerritory() {
    return lotlTerritory;
  }

  public void setLotlTerritory(String lotlTerritory) {
    this.lotlTerritory = lotlTerritory;
  }

  public BigInteger getVersion() {
    return version;
  }

  public void setVersion(BigInteger version) {
    this.version = version;
  }

  public BigInteger getHistoricalPeriod() {
    return historicalPeriod;
  }

  public void setHistoricalPeriod(BigInteger historicalPeriod) {
    this.historicalPeriod = historicalPeriod;
  }

  public String getEnglishSchemeName() {
    return englishSchemeName;
  }

  public void setEnglishSchemeName(String englishSchemeName) {
    this.englishSchemeName = englishSchemeName;
  }

  public String getEnglishLegalNotice() {
    return englishLegalNotice;
  }

  public void setEnglishLegalNotice(String englishLegalNotice) {
    this.englishLegalNotice = englishLegalNotice;
  }
}