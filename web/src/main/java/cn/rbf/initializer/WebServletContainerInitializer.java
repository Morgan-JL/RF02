package cn.rbf.initializer;

import cn.rbf.servlet.DefaultServlet;
import java.util.Set;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Morgan
 * @date 2021/4/1 09:14
 */
public class WebServletContainerInitializer implements ServletContainerInitializer {
  private static final Logger log= LoggerFactory.getLogger("c.l.w.i.RBFServlet");

  public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
    ServletRegistration.Dynamic RBFServlet = ctx.addServlet("RBFServlet", new DefaultServlet());
    RBFServlet.setLoadOnStartup(0);
    RBFServlet.addMapping("/");
    log.info("WebApplicationInitialize Add Servlet `name = RBFServlet mapping = [/] class = RBFServlet`");
  }
}
