package cn.rbf.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Morgan
 * @date 2020/12/10 16:21
 */
public class FileUtil {

  public static String readFileContent(InputStream in) {
    BufferedReader reader = null;
    StringBuffer sbf = new StringBuffer();
    try {
      reader = new BufferedReader(new InputStreamReader(in));
      String tempStr;
      while ((tempStr = reader.readLine()) != null) {
        sbf.append(tempStr + "\n");
      }
      reader.close();
      return sbf.toString();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }
    return sbf.toString();
  }


  public static String read(InputStream is) {

    byte[] bs = new byte[1024];
    //向字节数组中存储数据，返回读取的长度
    int len = 0;
    try {
      len = is.read(bs);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return new String(bs, 0, len);
  }

}
