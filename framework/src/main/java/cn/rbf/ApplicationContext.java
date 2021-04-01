//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf;

import cn.rbf.container.Module;
import cn.rbf.container.factory.Destroy;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public interface ApplicationContext extends Destroy {
  Object getBean(String id);

  Module getModule(String id);

  <T> List<T> getBean(Class<T>... aClass);

  List<Module> getModule(Class<?>... aClass);

  List<Object> getBeanByAnnotation(Class<? extends Annotation>... annotationClasses);

  List<Module> getModuleByAnnotation(Class<? extends Annotation>... annotationClasses);

  List<Object> getBeans();

  boolean isIOCType(String type);

  boolean isIOCId(String id);

  boolean isIOCClass(Class<?> componentClass);

  Set<Class<?>> getClasses(Class<?>...Class);
}
