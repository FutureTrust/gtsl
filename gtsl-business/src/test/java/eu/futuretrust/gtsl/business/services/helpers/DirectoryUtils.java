package eu.futuretrust.gtsl.business.services.helpers;

import java.io.File;
import java.util.stream.Stream;

public final class DirectoryUtils {

  private DirectoryUtils() {}

  public static Stream<File> getFiles(String directoryPath) {
    File folder = new File(directoryPath);
    if (!folder.isDirectory()) {
      return Stream.empty();
    }

    File[] files = folder.listFiles();
    if (files == null) {
      return Stream.empty();
    }

    return Stream.of(files).filter(File::isFile);
  }

}
