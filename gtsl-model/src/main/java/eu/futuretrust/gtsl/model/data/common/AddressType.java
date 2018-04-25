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

package eu.futuretrust.gtsl.model.data.common;

import eu.futuretrust.gtsl.jaxb.tsl.AddressTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.5 Scheme operator address
 *
 * <xsd:complexType name="AddressType"> <xsd:sequence> <!-- Specified in TS 119 612 v2.1.1 clause
 * 5.3.5.1 Scheme operator postal address --> <xsd:element ref="tsl:PostalAddresses"/> <!--
 * Specified in TS 119 612 v2.1.1 clause 5.3.5.2 Scheme operator electronic address --> <xsd:element
 * ref="tsl:ElectronicAddress"/> </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="PostalAddresses" type="tsl:PostalAddressListType"/> <xsd:element
 * name="ElectronicAddress" type="tsl:ElectronicAddressType"/>
 */
public class AddressType {

  @NotNull(message = "{NotNull.address.postalAddresses}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected PostalAddressListType postalAddresses;

  @NotNull(message = "{NotNull.address.electronicAddress}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected ElectronicAddressType electronicAddress;

  public AddressType() {
    this.postalAddresses = new PostalAddressListType();
    this.electronicAddress = new ElectronicAddressType();
  }

  public AddressType(AddressTypeJAXB addressJAXB) {
    if (addressJAXB != null) {
      if (addressJAXB.getPostalAddresses() != null) {
        this.postalAddresses = new PostalAddressListType(
            addressJAXB.getPostalAddresses());
      }
      if (addressJAXB.getElectronicAddress() != null) {
        this.electronicAddress = new ElectronicAddressType(
            addressJAXB.getElectronicAddress());
      }
    }
  }

  public AddressTypeJAXB asJAXB() {
    AddressTypeJAXB address = new AddressTypeJAXB();
    if (postalAddresses != null) {
      address.setPostalAddresses(postalAddresses.asJAXB());
    }
    if (electronicAddress != null) {
      address.setElectronicAddress(electronicAddress.asJAXB());
    }
    return address;
  }

  public PostalAddressListType getPostalAddresses() {
    return postalAddresses;
  }

  public void setPostalAddresses(PostalAddressListType postalAddresses) {
    this.postalAddresses = postalAddresses;
  }

  public ElectronicAddressType getElectronicAddress() {
    return electronicAddress;
  }

  public void setElectronicAddress(ElectronicAddressType electronicAddress) {
    this.electronicAddress = electronicAddress;
  }

}
