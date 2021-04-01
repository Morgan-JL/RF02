package cn.rbf;

import cn.rbf.base.Assert;
import cn.rbf.conf.ServerConfig;
import java.io.File;
import java.util.ServiceLoader;
import javax.servlet.ServletContainerInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.Tomcat;

/**
 * @author Morgan
 * @date 2020/12/10 15:11
 */
public class EmbedTomcat {

  private Tomcat tomcat;
  private ApplicationContext applicationContext;
  private ServerConfig serverConf;
  private Context context;

  public EmbedTomcat(Class<?> applicationClass, String[] args) {
    applicationContext = AutoScanApplicationContext.create(applicationClass);
    serverConf = RunParams.add(args);
    context = new StandardContext();
    tomcat = new Tomcat();
  }

  /**
   * 启动服务
   */
  public void run(){
    doShutDownWork();
    conf();
    init();
    start();
  }

  /**
   * 开始监听端口
   */
  public void await(){
    tomcat.getServer().await();
  }

  /**
   * 配置
   */
  private void conf(){
    tomcatConf();
    contextConf();
    tomcat.getHost().addChild(context);
  }

  /**
   * 启动Tomcat服务
   */
  private void start(){
    try {
      tomcat.getConnector();
      tomcat.init();
      tomcat.start();
    } catch (LifecycleException e) {
      e.printStackTrace();
    }
  }

  /**
   * 配置Tomcat
   */
  private void tomcatConf(){
    String baseDir = serverConf.getBaseDir();
    tomcat.setPort(serverConf.getPort());
    tomcat.setBaseDir(baseDir);
    File webapps = new File(baseDir + "webapps"+File.separator+"ROOT");
    if(!webapps.exists())webapps.mkdirs();
    tomcat.getHost().setAutoDeploy(serverConf.isAutoDeploy());
    if (Assert.isNotNull(serverConf.getClosePort())) {
      tomcat.getServer().setPort(serverConf.getClosePort());
    }
    if (Assert.isNotNull(serverConf.getShutdown())) {
      tomcat.getServer().setShutdown(serverConf.getShutdown());
    }
  }

  /**
   * 配置上下文
   */
  private void contextConf(){
    context.setSessionTimeout(serverConf.getSessionTimeout());
    context.setPath(serverConf.getContextPath());
    context.setReloadable(serverConf.isReloadable());
    context.setLoader(new WebappLoader(Thread.currentThread().getContextClassLoader()));
    String docBase = serverConf.getDocBase();
    if(docBase!=null){
      File docFile=new File(docBase);
      if(!docFile.exists()) {
        docFile.mkdirs();
      }
      context.setDocBase(docBase);
    }
    context.setSessionCookieName("RF-SESSION-ID");
    context.addLifecycleListener(new Tomcat.FixContextListener());
    context.addLifecycleListener(new Tomcat.DefaultWebXmlListener());
//        context.addLifecycleListener(new ContextConfig());
  }


  // 配置servlet组件
  private void init() {
    for (ServletContainerInitializer initializer : ServiceLoader
        .load(ServletContainerInitializer.class)) {
      context.addServletContainerInitializer(initializer, null);
    }
  }

  /**
   * Tomcat正常关闭时执行的销毁工作
   */
  private void doShutDownWork() {
    Runtime.getRuntime().addShutdownHook(new Thread(applicationContext::destroy));
  }

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }
}
