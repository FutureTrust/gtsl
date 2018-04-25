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

package eu.futuretrust.gtsl.model.data.tsp;

import eu.futuretrust.gtsl.jaxb.tsl.TSPInformationTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.common.AddressType;
import eu.futuretrust.gtsl.model.data.common.ExtensionsListType;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIListType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.4 TSP information
 *
 * <xsd:complexType name="TSPInformationType"> <xsd:sequence> <!-- Specified in TS 119 612 v2.1.1
 * clause 5.4.1 TSP name --> <xsd:element name="TSPName" type="tsl:InternationalNamesType"/> <!--
 * Specified in TS 119 612 v2.1.1 clause 5.4.2 TSP trade name --> <xsd:element name="TSPTradeName"
 * type="tsl:InternationalNamesType" minOccurs="0"/> <!-- Specified in TS 119 612 v2.1.1 clause
 * 5.4.3 TSP address. --> <xsd:element name="TSPAddress" type="tsl:AddressType"/> <!-- Specified in
 * TS 119 612 v2.1.1 clause 5.4.4 TSP information URI --> <xsd:element name="TSPInformationURI"
 * type="tsl:NonEmptyMultiLangURIListType"/> <!-- Specified in TS 119 612 v2.1.1 clause 5.4.5 TSP
 * information extensions --> <xsd:element name="TSPInformationExtensions"
 * type="tsl:ExtensionsListType" minOccurs="0"/> </xsd:sequence> </xsd:complexType>
 */
public class TSPInformationType {

  @NotNull(message = "{NotNull.tspInformation.tspName}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected InternationalNamesType tspName;

  @NotNull(message = "{NotNull.tspInformation.tspTradeName}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected InternationalNamesType tspTradeName;

  @NotNull(message = "{NotNull.tspInformation.tspAddress}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected AddressType tspAddress;

  @NotNull(message = "{NotNull.tspInformation.tspInformationURI}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected NonEmptyMultiLangURIListType tspInformationURI;

  @Valid
  protected ExtensionsListType tspInformationExtensions; // optional

  public TSPInformationType() {
    this.tspName = new InternationalNamesType();
    this.tspTradeName = new InternationalNamesType();
    this.tspAddress = new AddressType();
    this.tspInformationURI = new NonEmptyMultiLangURIListType();
    this.tspInformationExtensions = new ExtensionsListType();
  }

  public TSPInformationType(TSPInformationTypeJAXB tspInformation) {
    if (tspInformation != null) {
      if (tspInformation.getTSPName() != null) {
        this.tspName = new InternationalNamesType(tspInformation.getTSPName());
      } else {
        this.tspName = new InternationalNamesType();
      }
      if (tspInformation.getTSPTradeName() != null) {
        this.tspTradeName = new InternationalNamesType(tspInformation.getTSPTradeName());
      } else {
        this.tspTradeName = new InternationalNamesType();
      }
      if (tspInformation.getTSPAddress() != null) {
        this.tspAddress = new AddressType(tspInformation.getTSPAddress());
      } else {
        this.tspAddress = new AddressType();
      }
      if (tspInformation.getTSPInformationURI() != null) {
        this.tspInformationURI = new NonEmptyMultiLangURIListType(
            tspInformation.getTSPInformationURI());
      } else {
        this.tspInformationURI = new NonEmptyMultiLangURIListType();
      }
      if (tspInformation.getTSPInformationExtensions() != null) {
        this.tspInformationExtensions = new ExtensionsListType(
            tspInformation.getTSPInformationExtensions());
      } else {
        this.tspInformationExtensions = new ExtensionsListType();
      }
    }
  }

  public TSPInformationTypeJAXB asJAXB() {
    TSPInformationTypeJAXB tspInformation = new TSPInformationTypeJAXB();
    if (tspName != null) {
      tspInformation.setTSPName(tspName.asJAXB());
    }
    if (tspTradeName != null) {
      tspInformation.setTSPTradeName(tspTradeName.asJAXB());
    }
    if (tspAddress != null) {
      tspInformation.setTSPAddress(tspAddress.asJAXB());
    }
    if (tspInformationURI != null) {
      tspInformation.setTSPInformationURI(tspInformationURI.asJAXB());
    }
    if (tspInformationExtensions != null) {
      tspInformation.setTSPInformationExtensions(tspInformationExtensions.asJAXB());
    }
    return tspInformation;
  }

  public InternationalNamesType getTspName() {
    return tspName;
  }

  public void setTspName(InternationalNamesType tspName) {
    this.tspName = tspName;
  }

  public InternationalNamesType getTspTradeName() {
    return tspTradeName;
  }

  public void setTspTradeName(InternationalNamesType tspTradeName) {
    this.tspTradeName = tspTradeName;
  }

  public AddressType getTspAddress() {
    return tspAddress;
  }

  public void setTspAddress(AddressType tspAddress) {
    this.tspAddress = tspAddress;
  }

  public NonEmptyMultiLangURIListType getTspInformationURI() {
    return tspInformationURI;
  }

  public void setTspInformationURI(
      NonEmptyMultiLangURIListType tspInformationURI) {
    this.tspInformationURI = tspInformationURI;
  }

  public ExtensionsListType getTspInformationExtensions() {
    return tspInformationExtensions;
  }

  public void setTspInformationExtensions(
      ExtensionsListType tspInformationExtensions) {
    this.tspInformationExtensions = tspInformationExtensions;
  }

}
