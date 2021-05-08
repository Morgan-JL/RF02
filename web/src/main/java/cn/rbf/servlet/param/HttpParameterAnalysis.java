package cn.rbf.servlet.param;

import cn.rbf.reflect.ClassUtils;
import cn.rbf.servlet.Model;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Morgan
 * @date 2020/12/1 16:55
 */
public class HttpParameterAnalysis implements ParameterAnalysis {

  private final static Class<?>[] HTTP_CLASS=
      new Class[]{ServletRequest.class, ServletResponse.class, HttpSession.class,
          ServletContext.class, ServletConfig.class,Model.class};

  public boolean can(Model model, Method method, Parameter parameter, String asmParam) {
    return ClassUtils.isAssignableFromArrayOr(parameter.getType(),HTTP_CLASS);
  }

  public Object analysis(Model model, Method method, Parameter parameter, Type genericType,
      String asmParam)
      throws IOException {
    Class<?> parameterType = parameter.getType();
    if (ServletRequest.class.isAssignableFrom(parameterType)) {
      return model.getRequest();
    } else if (HttpSession.class.isAssignableFrom(parameterType)) {
      return model.getSession();
    } else if (ServletResponse.class.isAssignableFrom(parameterType)) {
      return model.getResponse();
    } else if (ServletContext.class.isAssignableFrom(parameterType)) {
      return model.getServletContext();
    } else if(ServletConfig.class.isAssignableFrom(parameterType)){
      return model.getServletConfig();
    }else {
      return model;
    }
  }
}
