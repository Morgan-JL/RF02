package cn.rbf.scan;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * @author Morgan
 * @date 2021/3/19 09:16
 */
public class ScanFactory {

  private static PackageScan packageScan;
  private static JarScan jarScan;

  public static Scan createScan(Class<?> applicationBootClass) {
    URL resource = ScanFactory.class.getClassLoader().getResource("");
    if (resource != null && !resource.getPath().contains(".jar!/")) {
      if (packageScan == null) {
        try {
          packageScan = new PackageScan(applicationBootClass);
        } catch (URISyntaxException e) {
          throw new RuntimeException(e);
        }
        packageScan.autoScan();
      }
      return packageScan;

    } else {
      if (jarScan == null) {
        try {
          jarScan = new JarScan(applicationBootClass);
        } catch (URISyntaxException e) {
          throw new RuntimeException(e);
        }
        jarScan.autoScan();
      }
      return jarScan;
    }
  }

}
