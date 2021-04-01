package cn.rbf.servlet;

import cn.rbf.container.RegisterMachine;
import cn.rbf.handler.RequestHandler;
import cn.rbf.route.RouteContainer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Morgan
 * @date 2020/12/2 9:46
 */
public class EntryServlet extends HttpServlet {

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    //请求处理
    RequestHandler.execute(req, resp);
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    doGet(req, resp);
  }

  public void init(ServletConfig config) throws ServletException {
    //初始化：加载jar包至class容器
    RegisterMachine registerMachine = new RegisterMachine();
    registerMachine.init();
    //路由容器初始化
    RouteContainer.getInstance();
  }
}
