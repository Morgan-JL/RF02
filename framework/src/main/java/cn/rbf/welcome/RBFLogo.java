package cn.rbf.welcome;

import cn.rbf.base.Console;
import cn.rbf.file.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Morgan
 * @date 2021/3/31 16:01
 */
public class RBFLogo {

  private static final Logger log= LoggerFactory.getLogger(RBFLogo.class);
  public final static String USER_LOGO_FILE= "/conf/logo.rbf";
  public final static String DEFAULT_LOGO_FILE= "/rbf-framework/RBF.rbf";
  public static boolean first = true;


  /**
   * 打印logo
   */
  public static void welcome() {
    if (!first)
      return;
    InputStream logoStream = Resources.getInputStream(USER_LOGO_FILE);
    if (logoStream != null) {
      print(USER_LOGO_FILE);
    }else{
      print(DEFAULT_LOGO_FILE);
    }
  }

  /**
   * 打印LOGO
   * @param filePath LOGO的文件位置
   */
  private static void print(String filePath){
    try {
      BufferedReader reader = Resources.getReader(filePath);
      Console.white(IOUtils.toString(reader));
      versionInfo();
      first = false;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private static int getMaxLength(String os, String java, String lucky) {
    int os_l = os.length();
    int java_l = java.length();
    int lucky_l = lucky.length();
    int temp = os_l > java_l ? os_l : java_l;
    return temp > lucky_l ? temp : lucky_l;
  }

  private static String getSameStr(String str, int maxLength) {
    if (str.length() == maxLength) {
      return str;
    }
    int poor = maxLength - str.length();
    for (int i = 0; i < poor; i++)
      str += " ";
    return str;
  }

  /**
   * 获取版本信息(OS,Java,RBF)
   * @return
   */
  public static void versionInfo() {
    String os = ":: " + System.getProperty("os.name");
    String osvsersion = "           :: (v" + System.getProperty("os.version") + ")";
    String java = ":: java";
    String javaversioin = "           :: (v" + System.getProperty("java.version") + ")";
    String rbf = ":: RBF APP";
    String rbfversion = "           :: ("+ Version.version()+")";
    int maxLength = getMaxLength(os, java, rbf);
    String d = "";
    Console.print("\n\n    ");
    Console.white( getSameStr(java, maxLength));Console.white(javaversioin);
    Console.print("\n    ");Console.white( getSameStr(rbf, maxLength));Console.white(rbfversion);
    Console.green("\n    ");Console.white( getSameStr(os, maxLength));Console.white(osvsersion+"\n\n");
  }

}
