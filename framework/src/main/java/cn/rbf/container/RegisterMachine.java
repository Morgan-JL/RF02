//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container;

import cn.rbf.annotations.Plugin;
import cn.rbf.annotations.Component;
import cn.rbf.annotations.Configuration;
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
  private final BeansContainer beanContainer;
  private final ConfigureContainer configureContainer;
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
          if (AnnotationUtils.strengthenIsExist(aClass, Plugin.class)) {
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
    Iterator componentIter = components.iterator();

    while (componentIter.hasNext()) {
      Class aClass = (Class) componentIter.next();

      if (!aClass.isInterface() && !Modifier.isAbstract(aClass.getModifiers())) {
        if (AnnotationUtils.isExist(aClass, Configuration.class)) {
          configureContainer.append(aClass.getSimpleName(), aClass);
        } else {
          String id = Namer.getBeanName(aClass);
          beanContainer.append(id, new Module(id, ClassUtils.newObject(aClass)));
        }
      }
    }

    BeanFactory factory;
    IocBeanFactory beanFactory;
    for (componentIter = ServiceLoader.load(BeanFactory.class).
        iterator(); componentIter.hasNext();
        beanFactory.add(factory.getClass())) {

      factory = (BeanFactory) componentIter.next();
      beanFactory = null;

      try {
        beanFactory = (IocBeanFactory) factory.getClass().newInstance();
      } catch (IllegalAccessException | InstantiationException e) {
        e.printStackTrace();
      }
    }

  }

  public void injection() {
    Collection<Module> modules = beanContainer.getBeans().values();
    Iterator moduleIterator = modules.iterator();

    while (moduleIterator.hasNext()) {
      Module module = (Module) moduleIterator.next();
      Injection.inject(module.getBean());
    }

  }

  public BeansContainer getBeanContainer(){
    return this.beanContainer;
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
