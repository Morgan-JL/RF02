package cn.rbf.servlet.param;

import cn.rbf.conversion.JavaConversion;
import cn.rbf.reflect.ClassUtils;
import cn.rbf.servlet.Model;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Morgan
 * @date 2020/12/1 16:33
 */
public class BaseParameterAnalysis implements ParameterAnalysis {

  public boolean can(Model model, Method method, Parameter parameter, String asmParam) {
    Class<?> parameterType = parameter.getType();

    return ClassUtils.isPrimitive(parameterType)
        || ClassUtils.isSimple(parameterType)
        || ClassUtils.isSimpleArray(parameterType);
  }

  public Object analysis(Model model, Method method, Parameter parameter, Type genericType, String asmParam)
      throws IOException {
    HttpServletRequest request = model.getRequest();
    String paramName = getParamName(parameter, asmParam);
    Class<?> parameterType = parameter.getType();
    if(ClassUtils.isSimple(parameterType)|| ClassUtils.isPrimitive(parameterType)){
      String strParam = request.getParameter(paramName);
      if(strParam!=null){
        return JavaConversion.strToBasic(strParam,parameterType);
      }
    }

    return null;
  }
}
