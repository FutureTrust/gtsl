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

import javax.validation.constraints.NotEmpty;

public class QualifierProperties {

  @NotEmpty
  private String qcWithSSCD;
  @NotEmpty
  private String qcNoSSCD;
  @NotEmpty
  private String qcSSCDStatusAsInCert;
  @NotEmpty
  private String qcForLegalPerson;
  @NotEmpty
  private String qcStatement;
  @NotEmpty
  private String qcWithQSCD;
  @NotEmpty
  private String qcNoQSCD;
  @NotEmpty
  private String qcQSCDStatusAsInCert;
  @NotEmpty
  private String qcQSCDManagedOnBehalf;
  @NotEmpty
  private String qcForESig;
  @NotEmpty
  private String qcForESeal;
  @NotEmpty
  private String qcForWSA;
  @NotEmpty
  private String notQualified;

  @Override
  public String toString() {
    return "QualifierProperties{" +
        "qcWithSSCD='" + qcWithSSCD + '\'' +
        ", qcNoSSCD='" + qcNoSSCD + '\'' +
        ", qcSSCDStatusAsInCert='" + qcSSCDStatusAsInCert + '\'' +
        ", qcForLegalPerson='" + qcForLegalPerson + '\'' +
        ", qcStatement='" + qcStatement + '\'' +
        ", qcWithQSCD='" + qcWithQSCD + '\'' +
        ", qcNoQSCD='" + qcNoQSCD + '\'' +
        ", qcQSCDStatusAsInCert='" + qcQSCDStatusAsInCert + '\'' +
        ", qcQSCDManagedOnBehalf='" + qcQSCDManagedOnBehalf + '\'' +
        ", qcForESig='" + qcForESig + '\'' +
        ", qcForESeal='" + qcForESeal + '\'' +
        ", qcForWSA='" + qcForWSA + '\'' +
        ", notQualified='" + notQualified + '\'' +
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

    QualifierProperties that = (QualifierProperties) o;

    if (qcWithSSCD != null ? !qcWithSSCD.equals(that.qcWithSSCD) : that.qcWithSSCD != null) {
      return false;
    }
    if (qcNoSSCD != null ? !qcNoSSCD.equals(that.qcNoSSCD) : that.qcNoSSCD != null) {
      return false;
    }
    if (qcSSCDStatusAsInCert != null ? !qcSSCDStatusAsInCert.equals(that.qcSSCDStatusAsInCert)
        : that.qcSSCDStatusAsInCert != null) {
      return false;
    }
    if (qcForLegalPerson != null ? !qcForLegalPerson.equals(that.qcForLegalPerson)
        : that.qcForLegalPerson != null) {
      return false;
    }
    if (qcStatement != null ? !qcStatement.equals(that.qcStatement) : that.qcStatement != null) {
      return false;
    }
    if (qcWithQSCD != null ? !qcWithQSCD.equals(that.qcWithQSCD) : that.qcWithQSCD != null) {
      return false;
    }
    if (qcNoQSCD != null ? !qcNoQSCD.equals(that.qcNoQSCD) : that.qcNoQSCD != null) {
      return false;
    }
    if (qcQSCDStatusAsInCert != null ? !qcQSCDStatusAsInCert.equals(that.qcQSCDStatusAsInCert)
        : that.qcQSCDStatusAsInCert != null) {
      return false;
    }
    if (qcQSCDManagedOnBehalf != null ? !qcQSCDManagedOnBehalf.equals(that.qcQSCDManagedOnBehalf)
        : that.qcQSCDManagedOnBehalf != null) {
      return false;
    }
    if (qcForESig != null ? !qcForESig.equals(that.qcForESig) : that.qcForESig != null) {
      return false;
    }
    if (qcForESeal != null ? !qcForESeal.equals(that.qcForESeal) : that.qcForESeal != null) {
      return false;
    }
    if (qcForWSA != null ? !qcForWSA.equals(that.qcForWSA) : that.qcForWSA != null) {
      return false;
    }
    return notQualified != null ? notQualified.equals(that.notQualified)
        : that.notQualified == null;
  }

  @Override
  public int hashCode() {
    int result = qcWithSSCD != null ? qcWithSSCD.hashCode() : 0;
    result = 31 * result + (qcNoSSCD != null ? qcNoSSCD.hashCode() : 0);
    result = 31 * result + (qcSSCDStatusAsInCert != null ? qcSSCDStatusAsInCert.hashCode() : 0);
    result = 31 * result + (qcForLegalPerson != null ? qcForLegalPerson.hashCode() : 0);
    result = 31 * result + (qcStatement != null ? qcStatement.hashCode() : 0);
    result = 31 * result + (qcWithQSCD != null ? qcWithQSCD.hashCode() : 0);
    result = 31 * result + (qcNoQSCD != null ? qcNoQSCD.hashCode() : 0);
    result = 31 * result + (qcQSCDStatusAsInCert != null ? qcQSCDStatusAsInCert.hashCode() : 0);
    result = 31 * result + (qcQSCDManagedOnBehalf != null ? qcQSCDManagedOnBehalf.hashCode() : 0);
    result = 31 * result + (qcForESig != null ? qcForESig.hashCode() : 0);
    result = 31 * result + (qcForESeal != null ? qcForESeal.hashCode() : 0);
    result = 31 * result + (qcForWSA != null ? qcForWSA.hashCode() : 0);
    result = 31 * result + (notQualified != null ? notQualified.hashCode() : 0);
    return result;
  }

  public String getQcWithSSCD() {
    return qcWithSSCD;
  }

  public void setQcWithSSCD(String qcWithSSCD) {
    this.qcWithSSCD = qcWithSSCD;
  }

  public String getQcNoSSCD() {
    return qcNoSSCD;
  }

  public void setQcNoSSCD(String qcNoSSCD) {
    this.qcNoSSCD = qcNoSSCD;
  }

  public String getQcSSCDStatusAsInCert() {
    return qcSSCDStatusAsInCert;
  }

  public void setQcSSCDStatusAsInCert(String qcSSCDStatusAsInCert) {
    this.qcSSCDStatusAsInCert = qcSSCDStatusAsInCert;
  }

  public String getQcForLegalPerson() {
    return qcForLegalPerson;
  }

  public void setQcForLegalPerson(String qcForLegalPerson) {
    this.qcForLegalPerson = qcForLegalPerson;
  }

  public String getQcStatement() {
    return qcStatement;
  }

  public void setQcStatement(String qcStatement) {
    this.qcStatement = qcStatement;
  }

  public String getQcWithQSCD() {
    return qcWithQSCD;
  }

  public void setQcWithQSCD(String qcWithQSCD) {
    this.qcWithQSCD = qcWithQSCD;
  }

  public String getQcNoQSCD() {
    return qcNoQSCD;
  }

  public void setQcNoQSCD(String qcNoQSCD) {
    this.qcNoQSCD = qcNoQSCD;
  }

  public String getQcQSCDStatusAsInCert() {
    return qcQSCDStatusAsInCert;
  }

  public void setQcQSCDStatusAsInCert(String qcQSCDStatusAsInCert) {
    this.qcQSCDStatusAsInCert = qcQSCDStatusAsInCert;
  }

  public String getQcQSCDManagedOnBehalf() {
    return qcQSCDManagedOnBehalf;
  }

  public void setQcQSCDManagedOnBehalf(String qcQSCDManagedOnBehalf) {
    this.qcQSCDManagedOnBehalf = qcQSCDManagedOnBehalf;
  }

  public String getQcForESig() {
    return qcForESig;
  }

  public void setQcForESig(String qcForESig) {
    this.qcForESig = qcForESig;
  }

  public String getQcForESeal() {
    return qcForESeal;
  }

  public void setQcForESeal(String qcForESeal) {
    this.qcForESeal = qcForESeal;
  }

  public String getQcForWSA() {
    return qcForWSA;
  }

  public void setQcForWSA(String qcForWSA) {
    this.qcForWSA = qcForWSA;
  }

  public String getNotQualified() {
    return notQualified;
  }

  public void setNotQualified(String notQualified) {
    this.notQualified = notQualified;
  }
}
