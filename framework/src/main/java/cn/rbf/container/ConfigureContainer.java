//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigureContainer extends Container<Class> {

  private static final Logger logger = LoggerFactory.getLogger(ConfigureContainer.class);

  private static ConfigureContainer INSTANCE = new ConfigureContainer();

  private ConfigureContainer() {
  }

  public static ConfigureContainer getInstance() {
    return INSTANCE;
  }

}
