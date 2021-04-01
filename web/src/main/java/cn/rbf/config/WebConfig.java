package cn.rbf.config;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import javax.servlet.Filter;

/**
 * @author Morgan
 * @date 2020/12/22 16:45
 */
public class WebConfig {

  private static WebConfig webConfig;

  private List<ServletMapping> servlets = new ArrayList<ServletMapping>();

  private List<FilterMapping> filters = new ArrayList<FilterMapping>();

  private List<ListenerMapping> listeners = new ArrayList<ListenerMapping>();

  public static WebConfig create() {
    if (webConfig == null) {
      webConfig = new WebConfig();
    }
    return webConfig;
  }

  public void addServlet(ServletMapping servlet) {
    servlets.add(servlet);
  }

  public void addFilter(FilterMapping filter) {
    filters.add(filter);
  }

  public void addListener(ListenerMapping listener) {
    listeners.add(listener);
  }

  public List<ServletMapping> getServlets() {
    return servlets;
  }

  public void setServlets(List<ServletMapping> servlets) {
    this.servlets = servlets;
  }

  public List<FilterMapping> getFilters() {
    return filters;
  }

  public void setFilters(List<FilterMapping> filters) {
    this.filters = filters;
  }

  public List<ListenerMapping> getListeners() {
    return listeners;
  }

  public void setListeners(List<ListenerMapping> listeners) {
    this.listeners = listeners;
  }
}
