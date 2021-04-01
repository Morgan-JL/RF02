package cn.rbf.servlet;

import java.util.Date;

/**
 * 响应数据返回格式封装
 *
 * @author Morgan
 * @date 2020/12/15 09:25
 */
public class Result {

  private String time;

  private int code;

  private Object content;

  public Result(){}

  public Result(String time, int code, Object content) {
    this.time = time;
    this.code = code;
    this.content = content;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public Object getContent() {
    return content;
  }

  public void setContent(Object content) {
    this.content = content;
  }
}
