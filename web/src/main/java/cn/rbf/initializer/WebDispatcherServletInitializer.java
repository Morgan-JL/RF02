package cn.rbf.initializer;

import cn.rbf.servlet.RBFDispatcherServlet;
import java.util.Set;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDispatcherServletInitializer implements ServletContainerInitializer {

  private static final Logger log = LoggerFactory
      .getLogger("c.l.w.i.WebDispatcherServletInitializer");


  public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
    //servletContext.addListener(new LuckyServletContextListener());
//        log.info("WebApplicationInitialize Add Listener `class = com.lucky.web.servlet.LuckyServletContextListener`");
    ServletRegistration.Dynamic servletRegistration = ctx
        .addServlet("RBFDispatcherServlet", new RBFDispatcherServlet());
    servletRegistration.setLoadOnStartup(0);
    servletRegistration.addMapping("/");
    servletRegistration.setAsyncSupported(true);
    log.info(
        "WebApplicationInitialize Add Servlet `name = RBFDispatcherServlet mapping = [/] class = cn.rbf.servlet.RBFDispatcherServlet`");
  }
}
