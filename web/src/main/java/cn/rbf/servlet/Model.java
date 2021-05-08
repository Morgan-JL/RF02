package cn.rbf.servlet;

import cn.rbf.enums.RequestMethod;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Morgan
 * @date 2020/12/1 16:00
 */
public class Model {

  private static final Logger log= LoggerFactory.getLogger(Model.class);

  /** 解码方式*/
  private String encod = "ISO-8859-1";
  private HttpServletRequest request;
  private HttpServletResponse response;
  /** 当前请求的URI*/
  private String uri;
  /*** ServletConfig对象*/
  private ServletConfig servletConfig;
  /*** url请求的方法*/
  private RequestMethod requestMethod;

  public Model(HttpServletRequest request, HttpServletResponse response,RequestMethod requestMethod) {
    this.requestMethod = requestMethod;
    init(request, response);
  }

  public void init(HttpServletRequest request, HttpServletResponse response){
    this.request = request;
    this.response = response;
    try {
      this.request.setCharacterEncoding("utf8");
      this.response.setCharacterEncoding("utf8");
      this.response.setHeader("Content-Type", "text/html;charset=utf-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    this.uri=request.getRequestURI();
  }


  public RequestMethod getRequestMethod() {
    return requestMethod;
  }

  /**
   * 得到Appliction对象
   *
   * @return
   */
  public ServletContext getServletContext() {
    return getRequest().getServletContext();
  }


  public HttpServletRequest getRequest() {
    return request;
  }

  /**
   * 得到ServletConfig对象
   *
   * @return
   */
  public ServletConfig getServletConfig() {
    return this.servletConfig;
  }


  public HttpServletResponse getResponse() {
    return response;
  }

  /**
   * 得到session对象
   *
   * @return
   */
  public HttpSession getSession() {
    return getRequest().getSession();
  }

  /**
   * 转发
   * @param address
   * @throws ServletException
   * @throws IOException
   */
  public void forward(String address) {
    try {
      getRequest().getRequestDispatcher(address).forward(getRequest(), getResponse());
    } catch (ServletException |IOException e) {
      throw new RuntimeException("转发失败，请检查转发地址！["+address+"]...",e);
    }
  }

  /**
   * 重定向
   * @param address
   * @throws IOException
   */
  public void redirect(String address) {
    try {
      getResponse().sendRedirect(address);
    } catch (IOException e) {
      throw new RuntimeException("重定向失败，请检查重定向地址！["+address+"]...",e);
    }
  }
}
