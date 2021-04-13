package cn.rbf.utils;

import java.util.List;

/**
 * 分页类
 * @author Morgan
 * @date 2020/6/3 12:55
 */
public class PageRes<T> {

  private int total;

  private int currentPage = 1;

  private int row = 10;

  private List<T> content;

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }
}
