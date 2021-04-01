package cn.rbf.mapping;

import java.util.EventListener;

/**
 * @author Morgan
 * @date 2020/12/24 10:38
 */
public class ListenerMapping {

  private String name;

  private EventListener listener;

  public ListenerMapping(String name, EventListener listener) {
    this.name  = name;
    this.listener = listener;
  }

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
