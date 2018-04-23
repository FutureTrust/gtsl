package eu.futuretrust.gtsl.business.services.api.impl;

import eu.futuretrust.gtsl.business.dto.helper.ResultDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.wrappers.ThrowingSupplier;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import java.security.InvalidParameterException;
import java.security.cert.CertificateException;
import java.util.Optional;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("apiPublic")
public class ApiPublicServiceImpl implements ApiService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiPublicServiceImpl.class);

  public <T> ResponseEntity<ResultDTO<T>> executeResponse(
      ThrowingSupplier<ResponseEntity<ResultDTO<T>>, Exception> supplier) {
    try {
      return supplier.get();
    } catch (UnauthorizedException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(e.getMessage());
      }
      ResultDTO<T> result = new ResultDTO<>();
      result.setErrorMessage("You are not authorized to proceed this action: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    } catch (InvalidParameterException | CertificateException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(e.getMessage());
      }
      ResultDTO<T> result = new ResultDTO<>();
      result.setErrorMessage("The provided input is invalid: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    } catch (Exception e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(e.getMessage());
        e.printStackTrace();
      }
      ResultDTO<T> result = new ResultDTO<>();
      result.setErrorMessage("An error occurred on the server: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
  }

}
