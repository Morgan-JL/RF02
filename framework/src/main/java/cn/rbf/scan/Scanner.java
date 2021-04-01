//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.scan;


import cn.rbf.container.ClassContainer;
import java.util.HashSet;
import java.util.Set;

public class Scanner {

  private static ClassContainer classContainer = ClassContainer.create();
  protected Set<Class<?>> allClass;
  protected Set<Class<?>> componentClass;
  /**
   * 被排除组件的类型
   */
  protected Set<Class<?>> exclusions;

  public Scanner() {
    allClass = new HashSet<>(225);
  }

  public static Set<Class<?>> getAllClasses() {
    return classContainer.getAllClass();
  }

  public static void scan(Class<?> clz) {
    classContainer.append(clz);
  }

}
