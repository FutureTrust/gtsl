package eu.futuretrust.gtsl.admin.validation;

import eu.futuretrust.gtsl.admin.helpers.ResourcesUtils;
import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.services.validator.TslValidator;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class TslValidationTest {

  @Autowired
  private TslValidator tslValidator;

  @Autowired
  private JaxbService jaxbService;

  private TrustStatusListType loadTsl(String fileName) throws IOException {
    InputStream is = ResourcesUtils.loadInputStream(fileName);
    Assert.assertNotNull(is);
    TrustStatusListTypeJAXB jaxb = this.jaxbService.unmarshallTsl(is);
    Assert.assertNotNull(jaxb);
    TrustStatusListType tsl = new TrustStatusListType(jaxb);
    Assert.assertNotNull(tsl);
    return tsl;
  }

  @Test
  public void validateEU() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - EU.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateAT() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - AT.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateBE() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - BE.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateBG() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - BG.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateCY() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - CY.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateCZ() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - CZ.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateDE() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - DE.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateDK() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - DK.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateEE() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - EE.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateEL() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - EL.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateES() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - ES.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateFI() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - FI.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateFR() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - FR.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateHR() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - HR.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateHU() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - HU.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateIE() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - IE.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateIS() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - IS.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateIT() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - IT.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateLI() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - LI.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateLT() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - LT.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateLU() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - LU.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateLV() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - LV.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateMT() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - MT.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateNL() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - NL.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateNO() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - NO.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validatePL() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - PL.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validatePT() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - PT.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateRO() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - RO.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateSE() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - SE.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateSI() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - SI.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateSK() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - SK.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

  @Test
  public void validateUK() throws Exception {
    TrustStatusListType tsl = loadTsl("tsl/TL - UK.xml");
    ReportDTO report = tslValidator.validate(tsl, null);
    Assert.assertTrue(report.isValid());
  }

}
