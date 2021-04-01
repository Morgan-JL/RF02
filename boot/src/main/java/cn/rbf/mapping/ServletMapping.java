package cn.rbf.mapping;

import javax.servlet.Servlet;

/**
 * @author Morgan
 * @date 2020/12/24 10:38
 */
public class ServletMapping extends WebMapping {

  private Servlet servlet;

  private String name;

  private int loadOnStartup = -1;

  public Servlet getServlet() {
    return servlet;
  }

  public void setServlet(Servlet servlet) {
    this.servlet = servlet;
  }

  public int getLoadOnStartup() {
    return loadOnStartup;
  }

  public void setLoadOnStartup(int loadOnStartup) {
    this.loadOnStartup = loadOnStartup;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
