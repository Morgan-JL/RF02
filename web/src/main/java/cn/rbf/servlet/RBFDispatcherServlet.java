package cn.rbf.servlet;

import cn.rbf.enums.RequestMethod;
import cn.rbf.handler.GlobalHandler;
import cn.rbf.route.BeanMapping;
import cn.rbf.route.RouteContainer;
import cn.rbf.servlet.param.ParameterAnalysisChain;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Morgan
 * @date 2021/5/8 10:40
 */
public class RBFDispatcherServlet extends BaseServlet {

  private static final Logger log = LoggerFactory.getLogger(RBFDispatcherServlet.class);

  protected void applyFor(HttpServletRequest req, HttpServletResponse resp, RequestMethod post) {
    Model model = null;
    try {
      model = new Model(req, resp, post);
      //获取路由容器
      RouteContainer routeContainer = RouteContainer.getInstance();
      //获取路由映射对象
      BeanMapping mapping = routeContainer.getBean(req.getServletPath());
      //路由参数解析
      Object[] runParam = new ParameterAnalysisChain().analysis(mapping.getMethod(), model);
      //执行方法
      Object res = mapping.run(runParam);
      //返回值处理
      model.getResponse().getWriter()
          .write(JSON.toJSONString(res, SerializerFeature.WriteNullStringAsEmpty));
    } catch (Throwable e) {
      //全局异常处理
      GlobalHandler.execute(model, e);
    }
  }
}
