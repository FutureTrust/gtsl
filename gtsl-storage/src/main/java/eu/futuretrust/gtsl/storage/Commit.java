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

package eu.futuretrust.gtsl.storage;

import java.math.BigInteger;

public class Commit {

  private String dataAddress;
  private String parent;
  private BigInteger versionNumber;

  public Commit() {
    this(null, BigInteger.ONE);
  }

  public Commit(String dataAddress, BigInteger versionNumber) {
    this(dataAddress, null, versionNumber);
  }

  public Commit(String dataAddress, String parent, BigInteger versionNumber) {
    this.dataAddress = dataAddress;
    this.parent = parent;
    this.versionNumber = versionNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Commit commit = (Commit) o;

    if (dataAddress != null ? !dataAddress.equals(commit.dataAddress)
        : commit.dataAddress != null) {
      return false;
    }
    if (parent != null ? !parent.equals(commit.parent) : commit.parent != null) {
      return false;
    }
    return versionNumber != null ? versionNumber.equals(commit.versionNumber)
        : commit.versionNumber == null;
  }

  @Override
  public int hashCode() {
    int result = dataAddress != null ? dataAddress.hashCode() : 0;
    result = 31 * result + (parent != null ? parent.hashCode() : 0);
    result = 31 * result + (versionNumber != null ? versionNumber.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Commit{" +
        "dataAddress='" + dataAddress + '\'' +
        ", parent='" + parent + '\'' +
        ", versionNumber='" + versionNumber + '\'' +
        '}';
  }

  public String getDataAddress() {
    return dataAddress;
  }

  public void setDataAddress(String dataAddress) {
    this.dataAddress = dataAddress;
  }

  public String getParent() {
    return this.parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public BigInteger getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(BigInteger versionNumber) {
    this.versionNumber = versionNumber;
  }
}
