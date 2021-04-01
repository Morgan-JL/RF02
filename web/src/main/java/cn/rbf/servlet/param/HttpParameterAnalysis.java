package cn.rbf.servlet.param;

import cn.rbf.servlet.Model;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * @author Morgan
 * @date 2020/12/1 16:55
 */
public class HttpParameterAnalysis implements ParameterAnalysis {

  public boolean can(Model model, Method method, Parameter parameter, String asmParam) {
    return false;
  }

  public Object analysis(Model model, Method method, Parameter parameter, Type genericType,
      String asmParam)
      throws IOException {
    return null;
  }
}
