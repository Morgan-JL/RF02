package cn.rbf.config;

import javax.servlet.Servlet;

/**
 * @author Morgan
 * @date 2020/12/24 10:38
 */
public class ServletMapping {

  private String name;

  private Servlet servlet;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Servlet getServlet() {
    return servlet;
  }

  public void setServlet(Servlet servlet) {
    this.servlet = servlet;
  }
}
