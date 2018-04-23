package eu.futuretrust.gtsl.business.services.api;

import eu.futuretrust.gtsl.business.dto.helper.ResultDTO;
import eu.futuretrust.gtsl.business.wrappers.ThrowingSupplier;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ApiService {

  <T> ResponseEntity<ResultDTO<T>> executeResponse(
      ThrowingSupplier<ResponseEntity<ResultDTO<T>>, Exception> supplier);

  default <T> ResponseEntity<ResultDTO<T>> execute(
      ThrowingSupplier<Optional<T>, Exception> supplier) {
    return executeResponse(
        () -> supplier.get()
            .map(val -> ResponseEntity.ok(new ResultDTO<>(val)))
            .orElse(ResponseEntity.notFound().build()));
  }

  default <T> ResponseEntity<ResultDTO<T>> execute(
      ThrowingSupplier<Optional<T>, Exception> supplier,
      HttpHeaders httpHeaders) {
    return executeResponse(
        () -> supplier.get()
            .map(val -> ResponseEntity.ok().headers(httpHeaders).body(new ResultDTO<>(val)))
            .orElse(ResponseEntity.notFound().build()));
  }

}
