package cn.rbf;

import cn.rbf.base.Assert;
import cn.rbf.conf.ServerConfig;
import cn.rbf.conversion.JavaConversion;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Morgan
 * @date 2020/12/14 16:03
 */
public class RunParams {

  private static final String SERVER_PORT = "server.port";

  private static RunParams RunParams;

  private static Map<String, String> runArgs;

  public static RunParams create() {
    if (RunParams == null) {
      RunParams = new RunParams();
    }
    return RunParams;
  }


  public static ServerConfig add(String[] args) {
    runArgs = new HashMap<>(20);
    List<String> list = Arrays.asList(args);
    for (String arg : list) {
      String[] param = arg.split("=");
      runArgs.put(param[0], param[1]);
    }

    ServerConfig serverConfig = ServerConfig.getServerConfig();
    String runPort = System.getProperty(SERVER_PORT);
    if (Assert.isNotNull(runPort)) {
      serverConfig.setPort((Integer) JavaConversion.strToBasic(runPort, int.class));
    }
    return serverConfig;
  }

  public static String get(String key) {
    return runArgs.get(key);
  }

}