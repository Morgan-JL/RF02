package cn.rbf.welcome;

import cn.rbf.file.Resources;
import java.io.IOException;

/**
 * @author Morgan
 * @date 2021/3/31 16:02
 */
public abstract class Version {

  private static final String RBF_VERSION = "/rbf-framework/Version.v";

  public static String version() {
    try {
      return Resources.getString(RBF_VERSION);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
