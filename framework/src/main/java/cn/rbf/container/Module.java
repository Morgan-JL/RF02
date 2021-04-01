//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container;

public class Module {
  private String id;
  private Object bean;
  private String type = "component";

  public Module(String id, Object bean) {
    this.id = id;
    this.bean = bean;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Object getBean() {
    return this.bean;
  }

  public void setBean(Object bean) {
    this.bean = bean;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
