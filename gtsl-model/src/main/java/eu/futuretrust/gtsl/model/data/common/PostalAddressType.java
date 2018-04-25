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

import eu.futuretrust.gtsl.jaxb.tsl.PostalAddressTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.5 Scheme operator address
 *
 * <xsd:complexType name="PostalAddressType"> <xsd:sequence> <xsd:element name="StreetAddress"
 * type="tsl:NonEmptyString"/> <xsd:element name="Locality" type="tsl:NonEmptyString"/> <xsd:element
 * name="StateOrProvince" type="tsl:NonEmptyString" minOccurs="0"/> <xsd:element name="PostalCode"
 * type="tsl:NonEmptyString" minOccurs="0"/> <xsd:element name="CountryName"
 * type="tsl:NonEmptyString"/> </xsd:sequence> <xsd:attribute ref="xml:lang" use="required"/>
 * </xsd:complexType>
 */
public class PostalAddressType implements MultiLang {

  @NotNull(message = "{NotNull.postalAddress.streetAddress}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected NonEmptyString streetAddress;

  @NotNull(message = "{NotNull.postalAddress.locality}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected NonEmptyString locality;

  @Valid
  protected StringType stateOrProvince; // optional

  @Valid
  protected StringType postalCode; // optional

  @NotNull(message = "{NotNull.postalAddress.countryName}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected CountryCode countryName;

  @NotNull(message = "{NotNull.postalAddress.lang}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected Lang lang;

  public PostalAddressType() {
    this.streetAddress = new NonEmptyString();
    this.locality = new NonEmptyString();
    this.stateOrProvince = new StringType();
    this.postalCode = new StringType();
    this.countryName = new CountryCode();
    this.lang = new Lang();
  }

  public PostalAddressType(PostalAddressTypeJAXB postalAddress) {
    if (postalAddress != null) {
      if (postalAddress.getStreetAddress() != null) {
        this.streetAddress = new NonEmptyString(postalAddress.getStreetAddress());
      }
      if (postalAddress.getLocality() != null) {
        this.locality = new NonEmptyString(postalAddress.getLocality());
      }
      if (postalAddress.getStateOrProvince() != null) {
        this.stateOrProvince = new StringType(postalAddress.getStateOrProvince());
      }
      if (postalAddress.getPostalCode() != null) {
        this.postalCode = new StringType(postalAddress.getPostalCode());
      }
      if (postalAddress.getCountryName() != null) {
        this.countryName = new CountryCode(postalAddress.getCountryName());
      }
      if (postalAddress.getLang() != null) {
        this.lang = new Lang(postalAddress.getLang());
      }
    }
  }

  public PostalAddressTypeJAXB asJAXB() {
    PostalAddressTypeJAXB postalAddress = new PostalAddressTypeJAXB();
    if (streetAddress != null && streetAddress.getValue() != null) {
      postalAddress.setStreetAddress(streetAddress.getValue());
    }
    if (locality != null && locality.getValue() != null) {
      postalAddress.setLocality(locality.getValue());
    }
    if (stateOrProvince != null && StringUtils.isNotEmpty(stateOrProvince.getValue())) {
      postalAddress.setStateOrProvince(stateOrProvince.getValue());
    }
    if (postalCode != null && StringUtils.isNotEmpty(postalCode.getValue())) {
      postalAddress.setPostalCode(postalCode.getValue());
    }
    if (countryName != null && countryName.getValue() != null) {
      postalAddress.setCountryName(countryName.getValue());
    }
    if (lang != null && lang.getValue() != null) {
      postalAddress.setLang(lang.getValue());
    }
    return postalAddress;
  }

  @Override
  public Lang getLang() {
    return lang;
  }

  @Override
  public void setLang(Lang lang) {
    this.lang = lang;
  }

  public NonEmptyString getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(NonEmptyString streetAddress) {
    this.streetAddress = streetAddress;
  }

  public NonEmptyString getLocality() {
    return locality;
  }

  public void setLocality(NonEmptyString locality) {
    this.locality = locality;
  }

  public StringType getStateOrProvince() {
    return stateOrProvince;
  }

  public void setStateOrProvince(StringType stateOrProvince) {
    this.stateOrProvince = stateOrProvince;
  }

  public StringType getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(StringType postalCode) {
    this.postalCode = postalCode;
  }

  public CountryCode getCountryName() {
    return countryName;
  }

  public void setCountryName(CountryCode countryName) {
    this.countryName = countryName;
  }

}
