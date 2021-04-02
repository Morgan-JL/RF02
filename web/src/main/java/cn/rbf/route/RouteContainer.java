package cn.rbf.route;

import cn.rbf.annotations.Controller;
import cn.rbf.annotations.Mapping;
import cn.rbf.container.Container;
import cn.rbf.container.RegisterMachine;
import cn.rbf.reflect.ClassUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 路由容器 [路由地址：映射类]
 *
 * @author Morgan
 * @date 2020/12/4 11:22
 */
public class RouteContainer extends Container<BeanMapping> {

  private static final Logger log = LoggerFactory.getLogger(RouteContainer.class);

  private static RouteContainer INSTANCE = new RouteContainer();

  private RouteContainer() {
  }

  public void rigOut(Class clz) {
    String controllerName = null;
    Controller controllerAnn = (Controller) clz.getAnnotation(Controller.class);
    if (controllerAnn != null) {
      controllerName = controllerAnn.url();

      Method[] methods = clz.getDeclaredMethods();
      for (Method method : methods) {
        // 扫描方法上带有[@Mapping]的注解
        if (method.getAnnotation(Mapping.class) != null) {
          controllerName = controllerName.startsWith("/") ? controllerName : "/" + controllerName;
          controllerName = controllerName.endsWith("/") ? controllerName : controllerName + "/";
          String mappingVal = method.getAnnotation(Mapping.class).value();
          mappingVal = mappingVal.startsWith("/") ? mappingVal.substring(1) : mappingVal;
          String beanId = controllerName + mappingVal;

          super.append(beanId,
              new BeanMapping(ClassUtils.newObject(clz), method));

          Map<String, String> paramInfo = new HashMap<>(10);
          for (Parameter parameter : method.getParameters()) {
            String type = parameter.getType().getName();
            String name = parameter.getName();
            paramInfo.put(type, name);
          }
          log.info("[{}] - params:{}", beanId, paramInfo);
        }
      }
    }
  }


  public static RouteContainer getInstance() {
    return INSTANCE;
  }

}
