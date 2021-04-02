package cn.rbf.beanFactory;

import cn.rbf.annotations.Controller;
import cn.rbf.annotations.Http;
import cn.rbf.container.RegisterMachine;
import cn.rbf.exception.HttpUrlAndKeyIsNoneException;
import cn.rbf.interceptor.HttpRequestInterceptor;
import cn.rbf.base.BaseUtils;
import cn.rbf.container.factory.IocBeanFactory;
import cn.rbf.proxy.CglibProxyFactory;
import cn.rbf.reflect.AnnotationUtils;
import cn.rbf.reflect.ClassUtils;
import cn.rbf.route.RouteContainer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Morgan
 * @date 2020/12/16 18:34
 */
public class RickWebBeanFactory extends IocBeanFactory {

  private static final Class[] ANNOTATION =
      new Class[]{Controller.class};

  public List<Object> createBean() {
    List<Object> beans = new ArrayList<>();
    Set<Class<?>> plugins = RegisterMachine.create().getPlugins();
    for (Class<?> aClass : plugins) {
      if (AnnotationUtils.isExist(aClass, Http.class)) {
        String host = null;
        String value = AnnotationUtils.get(aClass, Http.class).url();
        String key = AnnotationUtils.get(aClass, Http.class).key();
        if (!key.isEmpty()) {
//          host = PropertiesUtil.getProperty(key);
        } else if (!value.isEmpty()) {
          host = value;
        } else {
          throw new HttpUrlAndKeyIsNoneException(
              "[" + aClass + "] - {@HTTP}的url和key值都为空，请选择填写其中一个！");
        }
        HttpRequestInterceptor interceptor = new HttpRequestInterceptor();
        interceptor.setHost(host);
        beans.add(CglibProxyFactory.getProxy(aClass, interceptor));
      }

      if (AnnotationUtils.isExist(aClass, Controller.class)) {
        beans.add(ClassUtils.newObject(aClass));
        RouteContainer.getInstance().rigOut(aClass);
      }

    }

    return beans;
  }

  public String getBeanName(Class<?> beanClass) {
    if (!AnnotationUtils.isExistOrByArray(beanClass, ANNOTATION)) {
      return BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName());
    }
    Annotation controllerAnnotation = AnnotationUtils.getByArray(beanClass, ANNOTATION);
    String value = (String) AnnotationUtils.getValue(controllerAnnotation, "value");
    return value.isEmpty() ? BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName()) : value;
  }

}
