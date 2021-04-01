package cn.rbf.mapping;


import javax.servlet.DispatcherType;
import javax.servlet.Filter;

/**
 * @author Morgan
 * @date 2020/12/24 10:38
 */
public class FilterMapping extends WebMapping{

  private Filter filter;

  private String[] servletNames={};

  private DispatcherType[] dispatcherTypes={DispatcherType.REQUEST};

  public Filter getFilter() {
    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  public DispatcherType[] getDispatcherTypes() {
    return dispatcherTypes;
  }

  public void setDispatcherTypes(DispatcherType[] dispatcherTypes) {
    this.dispatcherTypes = dispatcherTypes;
  }

  public String[] getServletNames() {
    return servletNames;
  }

  public void setServletNames(String[] servletNames) {
    this.servletNames = servletNames;
  }
}
