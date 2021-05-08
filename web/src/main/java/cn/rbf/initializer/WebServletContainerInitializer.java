package cn.rbf.initializer;

import cn.rbf.reflect.ClassUtils;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Morgan
 * @date 2021/4/1 09:14
 */
@HandlesTypes(WebApplicationInitializer.class)
public class WebServletContainerInitializer implements ServletContainerInitializer {
  private static final Logger log= LoggerFactory.getLogger("c.l.w.i.WebServletContainerInitializer");

  public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext ctx) throws ServletException {
    List<WebApplicationInitializer> initializers = new LinkedList<>();

    if (webAppInitializerClasses != null) {
      for (Class<?> waiClass : webAppInitializerClasses) {
        // Be defensive: Some servlet containers provide us with invalid classes,
        // no matter what @HandlesTypes says...
        if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
            WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
          try {
            initializers.add((WebApplicationInitializer)
                ClassUtils.newObject(waiClass));
          }
          catch (Throwable ex) {
            throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
          }
        }
      }
    }

    if (initializers.isEmpty()) {
      log.info("No  WebApplicationInitializer types detected on classpath");
      return;
    }

    log.info(initializers.size() + "  WebApplicationInitializers detected on classpath");
    initializers=initializers.stream().sorted(Comparator.comparing(WebApplicationInitializer::priority)).collect(
        Collectors.toList());
    for (WebApplicationInitializer initializer : initializers) {
      initializer.onStartup(ctx);
    }
  }
}
