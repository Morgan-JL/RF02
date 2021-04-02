//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container;

import cn.hutool.core.util.ClassUtil;
import cn.rbf.annotations.Await;
import cn.rbf.annotations.Component;
import cn.rbf.annotations.Configuration;
import cn.rbf.base.BaseUtils;
import cn.rbf.reflect.AnnotationUtils;
import cn.rbf.container.factory.BeanFactory;
import cn.rbf.container.factory.BeanNamer;
import cn.rbf.container.factory.IocBeanFactory;
import cn.rbf.container.factory.Namer;
import cn.rbf.reflect.ClassUtils;
import cn.rbf.scan.Scan;
import cn.rbf.scan.Scanner;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterMachine {

  private static final Logger log = LoggerFactory.getLogger("cn.rbf.container.RegisterMachine");

  private static RegisterMachine registerMachine;
  private static BeansContainer beanContainer;
  private static ConfigureContainer configureContainer;
  private Set<Class<?>> plugins;
  private Set<Class<?>> components;
  private Set<Class<?>> allClasses;
  private Scan scan;
  private Namer namer = new BeanNamer();

  public RegisterMachine() {
    components = new HashSet<>();
    plugins = new HashSet<>();
    beanContainer = BeansContainer.getInstance();
    configureContainer = ConfigureContainer.getInstance();
  }

  public void setScan(Scan scan) {
    this.scan = scan;
    this.allClasses = Scanner.getAllClasses();
  }


  public static RegisterMachine create() {
    if (registerMachine == null) {
      registerMachine = new RegisterMachine();
    }

    return registerMachine;
  }

  public Set<Class<?>> getPlugins() {
    return plugins;
  }

  public void getComponents(Set<Class<?>> aClasses) {
    for (Class<?> aClass : aClasses) {
      if (!aClass.isAnnotation()) {
        if (AnnotationUtils.strengthenIsExist(aClass, Component.class)) {
          if (AnnotationUtils.strengthenIsExist(aClass, Await.class)) {
            plugins.add(aClass);
            continue;
          }
          components.add(aClass);
        }
      }
    }
  }

  public void init() {
    this.getComponents(allClasses);
    this.register();
    this.injection();
  }

  public void register() {
    Iterator var3 = components.iterator();

    while (var3.hasNext()) {
      Class aClass = (Class) var3.next();

      if (!aClass.isInterface() && !Modifier.isAbstract(aClass.getModifiers())) {
        if (AnnotationUtils.isExist(aClass, Configuration.class)) {
          configureContainer.append(aClass.getSimpleName(), aClass);
        } else {
          String id = this.namer.getBeanName(aClass);
          beanContainer.append(id, new Module(id, ClassUtils.newObject(aClass)));
        }
      }
    }

    BeanFactory factory;
    IocBeanFactory beanFactory;
    for (var3 = ServiceLoader.load(BeanFactory.class).

        iterator(); var3.hasNext();
        beanFactory.add(factory.getClass())) {

      factory = (BeanFactory) var3.next();
      beanFactory = null;

      try {
        beanFactory = (IocBeanFactory) factory.getClass().newInstance();
      } catch (IllegalAccessException | InstantiationException var7) {
        var7.printStackTrace();
      }
    }

  }

  public void injection() {
    Collection<Module> modules = beanContainer.getBeans().values();
    Iterator var2 = modules.iterator();

    while (var2.hasNext()) {
      Module module = (Module) var2.next();
      Injection.inject(module.getBean());
    }

  }

  public String getBeanName(Class<?> beanClass) {
    Namer namer = new Namer() {
      public String getBeanName(Class<?> beanClass) {
        return BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName());
      }

      public String getBeanType(Class<?> beanClass) {
        return "component";
      }
    };
    return namer.getBeanName(beanClass);
  }

  public Set<Class<?>> getClasses(Class<?>[] aClass) {
    Set<Class<?>> classes = new HashSet<>();
    for (Class<?> componentClass : allClasses) {
      for (Class<?> clz : aClass) {
        if (clz.isAnnotation()) {
          if (componentClass.isAnnotationPresent((Class<? extends Annotation>) clz)) {
            classes.add(componentClass);
          }
        } else {
          if (clz.isAssignableFrom(componentClass)) {
            classes.add(componentClass);
          }
        }
      }
    }
    return classes;
  }
}
