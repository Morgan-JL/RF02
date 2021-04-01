package cn.rbf.handler;

import cn.rbf.servlet.Model;
import cn.rbf.utils.FileUtil;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全局异常捕获处理
 *
 * @author Morgan
 * @date 2020/12/2 15:48
 */
public class GlobalHandler {

  public static void execute(Model model, Throwable e) {
    while (e.getCause() != null) {
      e = e.getCause();
    }
    writer(model, e);
  }

  /**
   * 将返回值以及错误、异常信息写入响应流返回
   *
   * @param model servlet信息存储对象
   * @param e 返回错误、异常信息
   */
  private static void writer(Model model, Throwable e) {
    try {
      InputStream is = GlobalHandler.class.getResourceAsStream("/static/500.html");
      String content = FileUtil.readFileContent(is);
      content = content.replaceAll("%ERROR%", e.toString());
      content = content.replaceAll("%errType%", "500");
      content = content
          .replaceAll("%time%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
      StringBuffer trace = new StringBuffer();
      for (StackTraceElement traceElement : e.getStackTrace()) {
        String replacement = null;
        replacement = traceElement.toString().replaceAll("\\$", "——>");
        trace.append(replacement + "<br>");
      }
      content = content.replaceAll("%STACKTRACE%", trace.toString());
      model.getResponse().getWriter().write(content);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}
