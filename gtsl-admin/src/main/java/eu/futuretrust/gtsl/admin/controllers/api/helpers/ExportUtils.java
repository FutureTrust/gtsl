package eu.futuretrust.gtsl.admin.controllers.api.helpers;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public final class ExportUtils {

  private ExportUtils() {
  }

  public static ResponseEntity<Resource> create(byte[] data, String filename) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
    return ResponseEntity.ok()
        .headers(httpHeaders)
        .body(new ByteArrayResource(data));
  }

}
