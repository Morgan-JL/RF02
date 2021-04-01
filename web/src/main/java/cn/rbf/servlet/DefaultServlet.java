package cn.rbf.servlet;

import cn.rbf.annotations.Component;
import cn.rbf.handler.RequestHandler;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Morgan
 * @date 2020/12/2 9:46
 */
@WebServlet
@Component
public class DefaultServlet extends HttpServlet {

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    //请求处理
    RequestHandler.execute(req, resp);
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    doGet(req, resp);
  }

}
