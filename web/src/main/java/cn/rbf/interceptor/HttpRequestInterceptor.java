package cn.rbf.interceptor;

import cn.hutool.http.HttpUtil;
import cn.rbf.annotations.Api;
import cn.rbf.annotations.ParamBody;
import cn.rbf.reflect.ClassUtils;
import com.alibaba.fastjson.JSON;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author cage
 * @date 2020/12/4 10:22
 */
public class HttpRequestInterceptor implements MethodInterceptor {

  /**
   * 远程服务器地址 http://ip:port
   */
  private String host;


  @Override
  public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
    Api annotation = method.getAnnotation(Api.class);
    String preMapping = annotation.value();

    //获取List<>中的泛型参数,用于反序列化
    Type type = method.getGenericReturnType();
    Map<String, Object> map = new HashMap<>();
    Parameter[] parameters = method.getParameters();
    String body = getParam(objects, map, parameters);
    return doPost(preMapping, map, body, type);
  }

  private String getParam(Object[] objects, Map<String, Object> map, Parameter[] parameters) {
    String body = "";
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      ParamBody paramBody = parameter.getAnnotation(ParamBody.class);
      if (paramBody != null && paramBody.required()) {
        body = JSON.toJSONString(objects[i]);
      } else {
        if (ClassUtils.isPrimitive(parameter.getType())
            || ClassUtils.isSimple(parameter.getType())
            || ClassUtils.isSimpleArray(parameter.getType())) {
          map.put(parameter.getName(), objects[i]);
        } else {
          try {
            Map<String, Object> map1 = objectToMap(objects[i]);
            map.putAll(map1);
          } catch (Exception e) {
            e.getMessage();
          }
        }
      }
    }
    return body;
  }

  public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
    Map<String, Object> map = new HashMap<>();
    Class<?> clazz = obj.getClass();
    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      String fieldName = field.getName();
      Object value = (field.get(obj));
      if (value != null) {
        map.put(fieldName, value);
      }
    }
    return map;
  }


  private Object doPost(String url, Map<String, Object> map, String body, Type type) {
    String needSeparation = url.startsWith("/") ? "" : "/";
    String paramString = map.toString().replaceAll(", ", "&");
    String urlString = host + needSeparation + url;
    if (!paramString.isEmpty()) {
      paramString = paramString.substring(1, paramString.length() - 1);
      urlString += "?" + paramString;
    }
    String result = HttpUtil.post(urlString, body);

    if (type == void.class) {
      return null;
    }

    return JSON.parseObject(result, type);
  }

  public void setHost(String host) {
    this.host = host;
  }
}
