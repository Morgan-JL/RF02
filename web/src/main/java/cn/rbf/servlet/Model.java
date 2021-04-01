package cn.rbf.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Morgan
 * @date 2020/12/1 16:00
 */
public class Model {

  private HttpServletRequest request;
  private HttpServletResponse response;

  public Model(HttpServletRequest request, HttpServletResponse response) {
    this.request = request;
    this.response = response;
    setEncoding();
  }

  private void setEncoding() {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=utf-8");
  }

  public HttpServletRequest getRequest() {
    return request;
  }

  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }

  public HttpServletResponse getResponse() {
    return response;
  }

  public void setResponse(HttpServletResponse response) {
    this.response = response;
  }
}
