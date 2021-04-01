package cn.rbf.config;

import javax.servlet.Filter;
import javax.servlet.Servlet;

/**
 * @author Morgan
 * @date 2020/12/24 10:38
 */
public class FilterMapping {

  private String name;

  private Filter filter;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Filter getFilter() {
    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }
}
