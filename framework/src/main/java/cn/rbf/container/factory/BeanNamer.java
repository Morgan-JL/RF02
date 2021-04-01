//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container.factory;

import cn.rbf.annotations.Component;
import cn.rbf.annotations.Configuration;
import cn.rbf.annotations.Repository;
import cn.rbf.annotations.Service;
import cn.rbf.base.Assert;
import cn.rbf.base.BaseUtils;
import cn.rbf.reflect.AnnotationUtils;
import java.lang.annotation.Annotation;
import java.util.List;

public class BeanNamer implements Namer {
  private Class[] COMPONENT_ANNOTATION = new Class[]{Component.class, Repository.class, Service.class, Configuration.class};

  public BeanNamer() {
  }

  public String getBeanName(Class<?> beanClass) {
    Annotation annotation = AnnotationUtils.getByArray(beanClass, this.COMPONENT_ANNOTATION);
    String value = (String)AnnotationUtils.getValue(annotation, "value");
    return !Assert.isBlankString(value) ? value : this.getDefBeanName(beanClass);
  }

  public String getBeanType(Class<?> beanClass) {
    List<Component> components = AnnotationUtils.strengthenGet(beanClass, Component.class);
    if (Assert.isEmptyCollection(components)) {
    }

    if (components.size() != 1) {
    }

    return ((Component)components.get(0)).type();
  }

  protected String getDefBeanName(Class<?> beanClass) {
    return BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName());
  }
}
