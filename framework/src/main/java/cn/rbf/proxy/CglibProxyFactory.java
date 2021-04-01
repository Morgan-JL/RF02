package cn.rbf.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author Morgan
 * @date 2021/4/1 11:17
 */
public class CglibProxyFactory {

  public static Object getProxy(Class<?> clazz, MethodInterceptor interceptor) {
    // 创建动态代理增强类
    Enhancer enhancer = new Enhancer();
    // 设置类加载器
    enhancer.setClassLoader(clazz.getClassLoader());
    // 设置被代理类
    enhancer.setSuperclass(clazz);
    // 设置方法拦截器
    enhancer.setCallback(interceptor);
    // 创建代理类
    return enhancer.create();
  }

}
