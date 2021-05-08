package cn.rbf.conf;

import cn.rbf.ApplicationContext;
import cn.rbf.base.Assert;
import cn.rbf.base.ObjectUtils;
import cn.rbf.confanalysis.RBFConf;
import cn.rbf.mapping.FilterMapping;
import cn.rbf.mapping.ListenerMapping;
import cn.rbf.mapping.ServletMapping;
import cn.rbf.reflect.AnnotationUtils;
import cn.rbf.reflect.ClassUtils;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

/**
 * @author Morgan
 * @date 2020/12/22 16:45
 */
public class ServerConfig extends RBFConf {

  private static ServerConfig serverConfig;

  private int port;

  /**
   * 项目路径
   */
  private String contextPath;
  /**
   * Session过期时间
   */
  private int sessionTimeout;
  /**
   * webapps目录中增加新的目录、war文件、修改WEB-INF/web.xml，autoDeploy="true"会新建或重新部署应用
   */
  private boolean autoDeploy;
  /**
   * 替换WEB-INF/lib目录中的jar文件或WEB-INF/classes目录中的class文件时，reloadable="true"会让修改生效
   */
  private boolean reloadable;
  /**
   * 关机命令的端口
   */
  private Integer closePort;
  /**
   * 关机命令
   */
  private String shutdown;

  private String webapp;

  private String URIEncoding;

  /**
   * 设置一个静态文件的储存库 1.${user.dir}/XXX System.getProperty("user.dir")下的某个文件夹 2.${java.io.tmpdir}/XXX
   * 系统临时文件夹下的某个文件夹 3.XXX 文件夹的绝对路径
   *
   * @param docbase
   */
  private String docBase;

  private String baseDir;

  private List<ServletMapping> servlets;

  private List<FilterMapping> filters;

  private List<ListenerMapping> listeners;

  public static ServerConfig defaultServerConfig() {
    if (serverConfig == null) {
      serverConfig = new ServerConfig();
      serverConfig.setPort(8080);
      serverConfig.setClosePort(null);
      serverConfig.setShutdown(null);
      serverConfig.setSessionTimeout(30);
      serverConfig.setWebapp("/WebContent");
      serverConfig.setContextPath("");
      serverConfig.setURIEncoding("UTF-8");
      serverConfig.setAutoDeploy(false);
      serverConfig.setReloadable(false);
      serverConfig.setFirst(true);
    }
    return serverConfig;
  }

  private ServerConfig() {
    servlets = new ArrayList<>();
    filters = new ArrayList<>();
    listeners = new ArrayList<>();
  }

  public static ServerConfig getServerConfig() {
    serverConfig = defaultServerConfig();
    if (serverConfig.isFirst()) {
      YamlParsing.loadServer(serverConfig);
    }
    return serverConfig;
  }

  public void init(ApplicationContext applicationContext) {
    servletRegister(applicationContext);
    filterRegister(applicationContext);
    listenerRegister(applicationContext);
  }

  /**
   * 注解版Servlet注册
   */
  private void servletRegister(ApplicationContext applicationContext) {
    Set<Class<?>> servletClasses = applicationContext.getClasses(WebServlet.class);
    if(ObjectUtils.isEmpty(servletClasses)){
      return;
    }
    for (Class<?> aClass : servletClasses) {
      WebServlet annServlet = aClass.getAnnotation(WebServlet.class);
      String name = annServlet.name();
      String[] urlPatterns = Assert.isEmptyArray(annServlet.urlPatterns()) ? annServlet.value()
          : annServlet.urlPatterns();
      if (Assert.isEmptyArray(urlPatterns)) {
        urlPatterns = new String[1];
        urlPatterns[0] = "/";
      }
      if (name.isEmpty()) {
        name = aClass.getSimpleName();
      }
      ServletMapping sm = new ServletMapping();
      sm.setName(name);
      sm.setServlet((Servlet) ClassUtils.newObject(aClass));
      sm.setUrlPatterns(urlPatterns);
      sm.setLoadOnStartup(annServlet.loadOnStartup());
      sm.setAsyncSupported(annServlet.asyncSupported());
      sm.setDescription(annServlet.description());
      sm.setDisplayName(annServlet.displayName());
      sm.setLargeIcon(annServlet.largeIcon());
      sm.setSmallIcon(annServlet.smallIcon());
      sm.setInitParams(annServlet.initParams());
      servlets.add(sm);
    }
  }

  /**
   * 注解版Filter注册
   */
  private void filterRegister(ApplicationContext applicationContext) {
    Set<Class<?>> filterClasses = applicationContext.getClasses(WebFilter.class);
    if(ObjectUtils.isEmpty(filterClasses)){
      return;
    }
    for (Class<?> aClass : filterClasses) {
      WebFilter annFilter = aClass.getAnnotation(WebFilter.class);
      String name = annFilter.filterName();
      if (name.isEmpty()) {
        name = aClass.getSimpleName();
      }
      String[] urlPatterns = Assert.isEmptyArray(annFilter.urlPatterns()) ? annFilter.value()
          : annFilter.urlPatterns();
      if (Assert.isEmptyArray(urlPatterns)) {
        urlPatterns = new String[1];
        urlPatterns[0] = "/";
      }
      FilterMapping fm = new FilterMapping();
      fm.setName(name);
      fm.setUrlPatterns(urlPatterns);
      fm.setFilter((Filter) ClassUtils.newObject(aClass));
      fm.setServletNames(annFilter.servletNames());
      fm.setDispatcherTypes(annFilter.dispatcherTypes());
      fm.setInitParams(annFilter.initParams());
      fm.setDescription(annFilter.description());
      fm.setDisplayName(annFilter.displayName());
      fm.setSmallIcon(annFilter.smallIcon());
      fm.setLargeIcon(annFilter.largeIcon());
      fm.setAsyncSupported(annFilter.asyncSupported());
      filters.add(fm);
    }
  }

  /**
   * 注解版Listener注册
   */
  private void listenerRegister(ApplicationContext applicationContext) {
    Set<Class<?>> listenerClasses = applicationContext.getClasses(WebListener.class);
    if(ObjectUtils.isEmpty(listenerClasses)){
      return;
    }
    for (Class<?> aClass : listenerClasses) {
      String name = AnnotationUtils.get(aClass, WebListener.class).value();
      if (name.isEmpty()) {
        name = aClass.getSimpleName();
      }
      ListenerMapping listenerMapping = new ListenerMapping(name,
          (EventListener) ClassUtils.newObject(aClass));
      listeners.add(listenerMapping);
    }
  }


  public void addListener(ListenerMapping listener) {
    this.listeners.add(listener);
  }

  public void addFilter(FilterMapping filter) {
    this.filters.add(filter);
  }

  public void addServlet(ServletMapping servlet) {
    this.servlets.add(servlet);
  }

  public List<ServletMapping> getServlets() {
    return servlets;
  }

  public List<FilterMapping> getFilters() {
    return filters;
  }

  public List<ListenerMapping> getListeners() {
    return listeners;
  }


  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getContextPath() {
    return contextPath;
  }

  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }

  public int getSessionTimeout() {
    return sessionTimeout;
  }

  public void setSessionTimeout(int sessionTimeout) {
    this.sessionTimeout = sessionTimeout;
  }

  public boolean isAutoDeploy() {
    return autoDeploy;
  }

  public void setAutoDeploy(boolean autoDeploy) {
    this.autoDeploy = autoDeploy;
  }

  public boolean isReloadable() {
    return reloadable;
  }

  public void setReloadable(boolean reloadable) {
    this.reloadable = reloadable;
  }

  public Integer getClosePort() {
    return closePort;
  }

  public void setClosePort(Integer closePort) {
    this.closePort = closePort;
  }

  public String getShutdown() {
    return shutdown;
  }

  public void setShutdown(String shutdown) {
    this.shutdown = shutdown;
  }

  public String getWebapp() {
    return webapp;
  }

  public void setWebapp(String webapp) {
    this.webapp = webapp;
  }

  public String getURIEncoding() {
    return URIEncoding;
  }

  public void setURIEncoding(String URIEncoding) {
    this.URIEncoding = URIEncoding;
  }

  public String getDocBase() {
    return docBase;
  }

  public void setDocBase(String docBase) {
    this.docBase = docBase;
  }

  public String getBaseDir() {
    return baseDir;
  }

  public void setBaseDir(String baseDir) {
    this.baseDir = baseDir;
  }

  public void loadYaml() {
    YamlParsing.loadServer(serverConfig);
  }
}
