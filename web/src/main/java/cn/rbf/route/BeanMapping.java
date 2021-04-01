package cn.rbf.route;

import java.lang.reflect.Method;

/**
 * @author Morgan
 * @date 2020/11/25 17:56
 */
public class BeanMapping {

  private Object bean;
  private Method method;

  public BeanMapping(Object bean, Method method) {
    this.bean = bean;
    this.method = method;
  }

  public BeanMapping() {
  }

  public Object getBean() {
    return bean;
  }

  public void setBean(Object bean) {
    this.bean = bean;
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public Object run(Object... objects) {
    try {
      return method.invoke(bean, objects);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
