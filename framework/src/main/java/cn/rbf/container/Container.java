package cn.rbf.container;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Morgan
 * @date 2021/3/31 14:15
 */
public abstract class Container<T> {

  private Map<String, T> beans;

  protected Container() {
    beans = new HashMap<>(20);
  }

  public Map<String, T> getBeans() {
    return beans;
  }

  public void append(String id, T t) {
    beans.put(id, t);
  }

  public T getBean(String id) {
    return beans.get(id);
  }

}
