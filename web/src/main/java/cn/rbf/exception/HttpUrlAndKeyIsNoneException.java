package cn.rbf.exception;

/**
 * bean未找到异常
 *
 * @author Morgan
 * @date 2020/12/8 16:28
 */
public class HttpUrlAndKeyIsNoneException extends RuntimeException {

  public HttpUrlAndKeyIsNoneException(String message) {
    super(message);
  }
}
