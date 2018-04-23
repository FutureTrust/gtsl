package eu.futuretrust.gtsl.ledger.helper;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;

public final class ResourcesUtils {

  private static ClassLoader CLASS_LOADER = ResourcesUtils.class.getClassLoader();

  private ResourcesUtils() {
  }

  public static InputStream loadInputStream(String file) {
    InputStream is = CLASS_LOADER.getResourceAsStream(file);
    Assert.assertNotNull(is);
    return is;
  }

  public static byte[] loadBytes(String file) throws IOException {
    byte[] bytes = IOUtils.toByteArray(loadInputStream(file));
    Assert.assertNotNull(bytes);
    return bytes;
  }

}
