package cn.rbf.servlet.param;

import cn.rbf.proxy.ASMUtil;
import cn.rbf.servlet.Model;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morgan
 * @date 2020/12/1 16:33
 */
public class ParameterAnalysisChain {

  private List<ParameterAnalysis> parameterAnalyseChain;

  public ParameterAnalysisChain() {
    parameterAnalyseChain = new ArrayList<>();
    parameterAnalyseChain.add(new RequestBodyParameterAnalysis());
    parameterAnalyseChain.add(new BaseParameterAnalysis());
    parameterAnalyseChain.add(new HttpParameterAnalysis());
    parameterAnalyseChain.add(new PojoParameterAnalysis());
  }

  public Object[] analysis(Method method, Model model) throws IOException {
    Parameter[] parameters = method.getParameters();
    String[] paramNames = ASMUtil.getMethodParamNames(method);
    Object[] runParam = new Object[parameters.length];
    Type[] genericParameterTypes = method.getGenericParameterTypes();
    for (int i = 0, j = parameters.length; i < j; i++) {
      for (ParameterAnalysis parameterAnalysis : parameterAnalyseChain) {
        if (parameterAnalysis.can(model, method, parameters[i], paramNames[i])) {
          runParam[i] = parameterAnalysis.analysis(model, method, parameters[i],genericParameterTypes[i], paramNames[i]);
          break;
        }
      }
    }
    return runParam;
  }

}
