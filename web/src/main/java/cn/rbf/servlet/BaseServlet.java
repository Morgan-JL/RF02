package cn.rbf.servlet;

import cn.rbf.annotations.Component;
import cn.rbf.enums.RequestMethod;
import cn.rbf.handler.RequestHandler;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Morgan
 * @date 2020/12/2 9:46
 */
public abstract class BaseServlet extends HttpServlet {

  private static final Logger log = LoggerFactory.getLogger(BaseServlet.class);

  /**
   * 前置处理 处理URL、请求类型和Web上下文的设置
   *
   * @param model 当前请求的Model
   */
  protected void beforeDispose(Model model) throws UnsupportedEncodingException {
//    webConfig.getMappingPreprocess().beforeDispose(model,webConfig);
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    this.applyFor(req, resp, RequestMethod.GET);
  }


  protected void doHead(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    this.applyFor(req, resp, RequestMethod.HEAD);
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    this.applyFor(req, resp, RequestMethod.POST);
  }

  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    this.applyFor(req, resp, RequestMethod.PUT);
  }

  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    this.applyFor(req, resp, RequestMethod.DELETE);
  }

  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    this.applyFor(req, resp, RequestMethod.OPTIOND);
  }

  protected void doTrace(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    this.applyFor(req, resp, RequestMethod.TRACE);
  }

  protected abstract void applyFor(HttpServletRequest req, HttpServletResponse resp,
      RequestMethod post);
}
