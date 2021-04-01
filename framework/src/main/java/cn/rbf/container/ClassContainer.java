//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassContainer {

  private static final Logger logger = LoggerFactory.getLogger(ClassContainer.class);
  private static ClassContainer classContainer;
  private Set<Class<?>> classes;

  private ClassContainer() {
    classes = new HashSet<>(255);
  }

  public static ClassContainer create() {
    if (classContainer == null) {
      classContainer = new ClassContainer();
    }

    return classContainer;
  }

  public Set<Class<?>> getAllClass() {
    return this.classes;
  }

  public void append(Class clz) {
    classes.add(clz);
  }

}

