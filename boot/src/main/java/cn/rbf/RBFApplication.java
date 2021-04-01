package cn.rbf;

import cn.rbf.base.Assert;
import cn.rbf.welcome.RBFLogo;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Morgan
 * @date 2021/3/31 16:00
 */
public class RBFApplication {

  private static Logger log;
  private static final RuntimeMXBean mxb = ManagementFactory.getRuntimeMXBean();

  static {
    System.setProperty("log4j.skipJansi", "false");
  }

  public static void run(Class<?> applicationClass, String[] args) {
    RBFLogo.welcome();
    long start = System.currentTimeMillis();
    log = LoggerFactory.getLogger(applicationClass);
    String pid = mxb.getName().split("@")[0];
    ThreadContext.put("pid", pid);
    String classpath = Assert.isNotNull(applicationClass.getClassLoader().getResource(""))
        ? applicationClass.getClassLoader().getResource("").getPath()
        : applicationClass.getResource("").getPath();
    log.info("Starting {} on localhost with PID {} ({} started by {} in {})"
        , applicationClass.getSimpleName()
        , pid
        , classpath
        , System.getProperty("user.name")
        , System.getProperty("user.dir"));
    EmbedTomcat tomcat = new EmbedTomcat(applicationClass, args);
    tomcat.run();
    long end = System.currentTimeMillis();
    log.info("Started {} in {} seconds (JVM running for {})"
        , applicationClass.getSimpleName()
        , (end - start) / 1000.0
        , mxb.getUptime() / 1000.0);
    tomcat.await();
  }
}
