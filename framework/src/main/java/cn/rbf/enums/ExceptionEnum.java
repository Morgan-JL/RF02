//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.enums;

public enum ExceptionEnum {
  BEANNOTFIND(501, "bean未在容器中找到！");

  private Integer code;
  private String message;

  private ExceptionEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public Integer getCode() {
    return this.code;
  }

  public String getMessage() {
    return this.message;
  }
}
