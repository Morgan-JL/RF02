package cn.rbf.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public abstract class CglibProxy {

    private static final String PROXY_NAME="$$LUCKY_PROXY$$";

    public static <T> T getCglibProxyObject(Class<T> clazz, MethodInterceptor methodInterceptor){
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setNamingPolicy(new LuckyNamingPolicy());
        enhancer.setCallback(methodInterceptor);
        return (T) enhancer.create();
    }

    public static boolean isAgent(Class<?> aClass){
        return aClass.getName().contains(PROXY_NAME);
    }
}



