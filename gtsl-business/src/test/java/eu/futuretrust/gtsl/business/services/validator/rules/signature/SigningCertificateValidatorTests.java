package eu.futuretrust.gtsl.business.services.validator.rules.signature;

import eu.futuretrust.gtsl.business.services.validator.rules.signature.impl.CertificateValidatorImpl;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SigningCertificateValidatorTests {


  private static ClassLoader classLoader;
  private static CertificateFactory factory;
  private static final File resourcesDirectory = new File("src/test/resources");

  @BeforeClass
          public static void init() throws CertificateException
  {
    classLoader = SigningCertificateValidatorTests.class.getClassLoader();
    factory = CertificateFactory.getInstance("X.509");
  }

  @Test
  public void isSigningCertificateKeyUsageExtensionConsistentTest() throws CertificateException, FileNotFoundException
  {

    final ValidationContext validationContext = new ValidationContext(null, null);
    final CertificateValidatorImpl validator = new CertificateValidatorImpl();

    final InputStream validCertIs = new FileInputStream(new File(resourcesDirectory.getAbsolutePath() + "/certificates/be-cert.cer"));
    final InputStream invalidCertIs = new FileInputStream(new File(resourcesDirectory.getAbsolutePath() + "/certificates/dk-cert.cer"));

    final X509Certificate validCertificate = (X509Certificate) factory.generateCertificate(validCertIs);
    final X509Certificate invalidCertificate = (X509Certificate) factory.generateCertificate(invalidCertIs);

    validator.isSigningCertificateKeyUsageConsistent(
            validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_KEY_USAGE_CONSISTENT, validCertificate);
    validator.isSigningCertificateKeyUsageConsistent(
            validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_KEY_USAGE_CONSISTENT, invalidCertificate);

    List<Violation> report = validationContext.getReport();
    Assert.assertEquals(report.size(), 1);
    Assert.assertEquals(report.get(0).getViolation(), ViolationConstant.IS_SIGNING_CERTIFICATE_KEY_USAGE_CONSISTENT);
  }

 @Test
  public void isSigningCertificateBasicConstraintsExtensionValidTest() throws CertificateException, FileNotFoundException
 {

    final ValidationContext validationContext = new ValidationContext(null, null);
    final CertificateValidator validator = new CertificateValidatorImpl();

   final InputStream validCertIs = new FileInputStream(new File(resourcesDirectory.getAbsolutePath() + "/certificates/be-cert.cer"));
    InputStream invalidCertIs = new FileInputStream(new File(resourcesDirectory.getAbsolutePath() + "/certificates/luxtrust-ca.cer"));

    final X509Certificate validCertificate = (X509Certificate) factory.generateCertificate(validCertIs);
    final X509Certificate invalidCertificate = (X509Certificate) factory.generateCertificate(invalidCertIs);

    validator.isSigningCertificateBasicConstraintsExtensionValid(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_BASIC_CONSTRAINTS_EXTENSION_VALID, validCertificate);
    validator.isSigningCertificateBasicConstraintsExtensionValid(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_BASIC_CONSTRAINTS_EXTENSION_VALID, invalidCertificate);

    List<Violation> report = validationContext.getReport();
    Assert.assertEquals(report.size(), 1);
    Assert.assertEquals(report.get(0).getViolation(), ViolationConstant.IS_SIGNING_CERTIFICATE_BASIC_CONSTRAINTS_EXTENSION_VALID);
  }

  @Test
  public void isSigningCertificateSKIExtensionPresentTest() throws CertificateException, FileNotFoundException
  {
    final ValidationContext validationContext = new ValidationContext(null, null);
    final CertificateValidator validator = new CertificateValidatorImpl();

    final InputStream validCertIs = new FileInputStream(new File(resourcesDirectory.getAbsolutePath() + "/certificates/be-cert.cer"));
    final InputStream invalidCertIs = new FileInputStream(new File(resourcesDirectory.getAbsolutePath() + "/certificates/no-ski.cer"));
    X509Certificate validCertificate = (X509Certificate) factory.generateCertificate(validCertIs);
    X509Certificate invalidCertificate = (X509Certificate) factory.generateCertificate(invalidCertIs);

    validator.isSigningCertificateSKIExtensionPresent(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_SKI_PRESENT, validCertificate);
    validator.isSigningCertificateSKIExtensionPresent(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_SKI_PRESENT, invalidCertificate);

    List<Violation> report = validationContext.getReport();
    Assert.assertEquals(report.size(), 1);
    Assert.assertEquals(report.get(0).getViolation(), ViolationConstant.IS_SIGNING_CERTIFICATE_SKI_PRESENT);
  }

  @Test
  public void isSigningCertificateCountryCodeValidTest() throws CertificateException
  {
    final ValidationContext validationContext = new ValidationContext(null, null);
    final CertificateValidator validator = new CertificateValidatorImpl();

    final InputStream validCertIs = classLoader.getResourceAsStream("certificates/be-cert.cer");
    final InputStream invalidCertIs = classLoader.getResourceAsStream("certificates/at-cert.cer");

    X509Certificate validCert = (X509Certificate) factory.generateCertificate(validCertIs);
    X509Certificate invalidCert = (X509Certificate) factory.generateCertificate(invalidCertIs);

    validator.isSigningCertificateCountryCodeValid(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_COUNTRY_CODE_VALID, validCert, "BE");
    validator.isSigningCertificateCountryCodeValid(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_COUNTRY_CODE_VALID, invalidCert, "BE");

    List<Violation> report = validationContext.getReport();

    Assert.assertEquals(report.size(), 1);
    Assert.assertEquals(report.get(0).getViolation(), ViolationConstant.IS_SIGNING_CERTIFICATE_COUNTRY_CODE_VALID);
  }

  @Test
  public void hasSigningCertificateExtendedKeyUsageTslSigningTest() throws CertificateException
  {

    final ValidationContext validationContext = new ValidationContext(null, null);
    final CertificateValidator validator = new CertificateValidatorImpl();

    final InputStream validCertIs = classLoader.getResourceAsStream("certificates/be-cert.cer");
    final InputStream invalidCertIs = classLoader.getResourceAsStream("certificates/at-cert.cer");

    X509Certificate validCert = (X509Certificate) factory.generateCertificate(validCertIs);
    X509Certificate invalidCert = (X509Certificate) factory.generateCertificate(invalidCertIs);

    validator.hasSigningCertificateExtendedKeyUsageTslSigning(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_EXTENDED_KEY_USAGE_PRESENT, validCert);
    validator.hasSigningCertificateExtendedKeyUsageTslSigning(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_EXTENDED_KEY_USAGE_PRESENT, invalidCert);

    List<Violation> report = validationContext.getReport();

    Assert.assertEquals(report.size(), 1);
    Assert.assertEquals(report.get(0), ViolationConstant.IS_SIGNING_CERTIFICATE_EXTENDED_KEY_USAGE_PRESENT);
  }
}
