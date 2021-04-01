//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf;

import cn.rbf.container.BeansContainer;
import cn.rbf.container.Module;
import cn.rbf.container.RegisterMachine;
import cn.rbf.container.factory.Destroy;
import cn.rbf.scan.Scan;
import cn.rbf.scan.ScanFactory;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AutoScanApplicationContext implements ApplicationContext {
  private static AutoScanApplicationContext autoScanApplicationContext;
  public Class<?> applicationBootClass;
  private static BeansContainer beanContainer = BeansContainer.getInstance();
  private static RegisterMachine registerMachine = RegisterMachine.create();

  private AutoScanApplicationContext(Class<?> applicationBootClass) {
    this.applicationBootClass = applicationBootClass;
    init();
  }

  private AutoScanApplicationContext(){
    init();
  }

  private void init(){
    Scan scan= ScanFactory.createScan(applicationBootClass);
    registerMachine=RegisterMachine.create();
    registerMachine.setScan(scan);
    registerMachine.init();
  }


  public static AutoScanApplicationContext create(){
    if(autoScanApplicationContext==null){
      autoScanApplicationContext=new AutoScanApplicationContext();
    }
    return autoScanApplicationContext;
  }

  public static AutoScanApplicationContext create(Class<?> applicationBootClass){
    if(autoScanApplicationContext==null){
      autoScanApplicationContext=new AutoScanApplicationContext(applicationBootClass);
    }
    return autoScanApplicationContext;
  }


  public Object getBean(String id) {
    Object bean = null;
    Collection<Module> modules = beanContainer.getBeans().values();
    Iterator var4 = modules.iterator();

    while(var4.hasNext()) {
      Module module = (Module)var4.next();
      if (module.getId().equals(id)) {
        bean = module.getBean();
      }
    }

    return bean;
  }

  public Module getModule(String id) {
    Collection<Module> modules = beanContainer.getBeans().values();
    Iterator var3 = modules.iterator();

    Module module;
    do {
      if (!var3.hasNext()) {
        return null;
      }

      module = (Module)var3.next();
    } while(!module.getId().equals(id));

    return module;
  }

  public <T> List<T> getBean(Class<T>... aClass) {
    return null;
  }

  public List<Module> getModule(Class<?>... aClass) {
    return null;
  }

  public List<Object> getBeanByAnnotation(Class<? extends Annotation>... annotationClasses) {
    return null;
  }

  public List<Module> getModuleByAnnotation(Class<? extends Annotation>... annotationClasses) {
    return null;
  }

  public List<Object> getBeans() {
    List<Object> beans = new ArrayList(10);
    Collection<Module> modules = beanContainer.getBeans().values();
    Iterator var3 = modules.iterator();

    while(var3.hasNext()) {
      Module module = (Module)var3.next();
      beans.add(module.getBean());
    }

    return beans;
  }

  public boolean isIOCType(String type) {
    return false;
  }

  public boolean isIOCId(String id) {
    return false;
  }

  public boolean isIOCClass(Class<?> componentClass) {
    return false;
  }

  public Set<Class<?>> getClasses(Class<?>... Class) {
    return   registerMachine.getClasses(Class);
  }

  public void destroy() {
    getBean(Destroy.class).stream().forEach(d->d.destroy());
  }
}
