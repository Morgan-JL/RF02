package cn.rbf.servlet.param;

import cn.rbf.annotations.RequestBody;
import cn.rbf.file.FileUtils;
import cn.rbf.reflect.AnnotationUtils;
import cn.rbf.servlet.Model;
import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * @author Morgan
 * @date 2020/12/1 16:22
 */
public class RequestBodyParameterAnalysis implements ParameterAnalysis {

  public boolean can(Model model, Method method, Parameter parameter, String asmParam) {
    return AnnotationUtils.isExist(parameter, RequestBody.class);
  }

  public Object analysis(Model model, Method method, Parameter parameter, Type genericType,
      String asmParam)
      throws IOException {
    BufferedReader br = new BufferedReader(
        new InputStreamReader(model.getRequest().getInputStream(), "UTF-8"));
    StringWriter sw = new StringWriter();
    FileUtils.copy(br, sw);
    String requestBody = sw.toString();
    try {
      return JSON.parseObject(requestBody, genericType);
    } catch (Exception e) {
      throw new RuntimeException("错误的参数格式");
    }
  }
}
