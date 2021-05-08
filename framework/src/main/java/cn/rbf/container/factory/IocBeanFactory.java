//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container.factory;

import cn.rbf.base.BaseUtils;
import cn.rbf.container.BeansContainer;
import cn.rbf.container.Module;
import cn.rbf.container.RegisterMachine;
import cn.rbf.reflect.AnnotationUtils;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class IocBeanFactory implements BeanFactory, Namer {

  private BeansContainer beanContainer = RegisterMachine.create().getBeanContainer();
  private Set<Class<?>> plugins = RegisterMachine.create().getPlugins();

  public IocBeanFactory() {
  }

  public final void add(Class aClass) {
//    Assert.isNull(this.createBean(), "当前" + aClass + "添加的bean为空！");
    this.createBean().forEach((bean) -> {
      String id = this.getBeanName(bean.getClass());
      beanContainer.append(id, new Module(id, bean));
    });
  }

  public List<Object> createBean() {
    return new ArrayList<>();
  }

  public List<Module> getBeanByClass(Class<?>... componentClasses) {
    List<Module> modules = new ArrayList<>();
    for (Class<?> aClass : componentClasses) {
      modules.add((Module) beanContainer.getBean(aClass));
    }
    return modules;
  }

  public String getBeanType(Class<?> beanClass) {
    return "component";
  }

  public String getBeanName(Class<?> beanClass) {
    return BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName());
  }

  public List<Class<?>> getPluginByAnnotation(Class<? extends Annotation>...annotationClasses){
    return plugins.stream()
        .filter((a)->{
          for (Class<? extends Annotation> annotationClass : annotationClasses) {
            if(AnnotationUtils.isExist(a,annotationClass)){
              return true;
            }
          }
          return false;
        })
        .collect(Collectors.toList());
  }
}
