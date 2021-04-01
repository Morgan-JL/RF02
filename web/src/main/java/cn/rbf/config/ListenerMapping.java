package cn.rbf.config;

import java.util.EventListener;
import javax.servlet.Servlet;

/**
 * @author Morgan
 * @date 2020/12/24 10:38
 */
public class ListenerMapping {

  private String name;

  private EventListener listener;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public EventListener getListener() {
    return listener;
  }

  public void setListener(EventListener listener) {
    this.listener = listener;
  }
}
