//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container.factory;

import cn.rbf.annotations.Bean;
import cn.rbf.container.ConfigureContainer;
import cn.rbf.reflect.AnnotationUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ConfigurationBeanFactory extends IocBeanFactory {

  public List<Object> createBean() {
    List<Object> beans = new ArrayList(20);
    ConfigureContainer configureContainer = ConfigureContainer.getInstance();
    Collection<Class> configClzs = configureContainer.getBeans().values();
    Iterator var4 = configClzs.iterator();

    while (var4.hasNext()) {
      Class aClass = (Class) var4.next();
      Method[] methods = aClass.getDeclaredMethods();
      Method[] var7 = methods;
      int var8 = methods.length;

      for (int var9 = 0; var9 < var8; ++var9) {
        Method method = var7[var9];
        if (AnnotationUtils.isExist(method, Bean.class)) {
          Object bean = null;

          try {
            bean = method.invoke(aClass.newInstance(), method.getParameters());
            beans.add(bean);
          } catch (InstantiationException | InvocationTargetException | IllegalAccessException var13) {
            var13.printStackTrace();
          }
        }
      }
    }

    return beans;
  }

  public String getBeanName(Class<?> beanClass) {
    return super.getBeanName(beanClass);
  }
}
