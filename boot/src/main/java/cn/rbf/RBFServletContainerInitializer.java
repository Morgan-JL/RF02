package cn.rbf;

import cn.rbf.base.Assert;
import cn.rbf.conf.ServerConfig;
import cn.rbf.mapping.FilterMapping;
import cn.rbf.mapping.ListenerMapping;
import cn.rbf.mapping.ServletMapping;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Morgan
 * @date 2020/12/15 18:31
 */
public class RBFServletContainerInitializer implements ServletContainerInitializer {

  private static final Logger log = LoggerFactory.getLogger("c.s.RBFServletContainerInitializer");

  private final static ServerConfig serverConfig = ServerConfig.getServerConfig();

  public RBFServletContainerInitializer() {
    serverConfig.init(AutoScanApplicationContext.create());
  }

  public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
    ServletRegistration.Dynamic servlet;
    FilterRegistration.Dynamic filter;

    for (ServletMapping sm : serverConfig.getServlets()) {
      servlet = servletContext
          .addServlet(sm.getName(), sm.getServlet());
      servlet.setLoadOnStartup(sm.getLoadOnStartup());
      servlet.setAsyncSupported(sm.isAsyncSupported());
      servlet.setInitParameters(sm.getInitParams());
      servlet.addMapping(sm.getUrlPatterns());
      log.info("Add Servlet `name="+sm.getName()+" mapping="+ Arrays.toString(sm.getUrlPatterns())+" class="+sm.getServlet().getClass().getName()+"`");
    }

    for (FilterMapping fm : serverConfig.getFilters()) {
      DispatcherType dispatcherTypes = fm.getDispatcherTypes()[0];
      filter = servletContext.addFilter(fm.getName(),  fm.getFilter());
      filter.addMappingForUrlPatterns(EnumSet.of(dispatcherTypes), true,
          fm.getUrlPatterns());
      String[] servletNames = fm.getServletNames();
      if (!Assert.isEmptyArray(servletNames)) {
        filter
            .addMappingForServletNames(EnumSet.of(dispatcherTypes), true, fm.getServletNames());
      }
      log.info("Add Filter `name="+fm.getName()+" mapping="+Arrays.toString(fm.getUrlPatterns())+" class="+ fm.getFilter().getClass().getName()+"`");
    }

    for (ListenerMapping lm : serverConfig.getListeners()) {
      servletContext.addListener(lm.getListener());
      log.info("Add Listener `class="+lm.getListener().getClass().getName()+"`");
    }
  }


}
