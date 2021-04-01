package cn.rbf.handler;

import cn.rbf.route.BeanMapping;
import cn.rbf.route.RouteContainer;
import cn.rbf.servlet.Model;
import cn.rbf.servlet.param.ParameterAnalysisChain;
import cn.rbf.utils.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 * 请求处理
 *
 * @author Morgan
 * @date 2020/12/8 11:24
 */
public class RequestHandler {

  public static void execute(HttpServletRequest req, HttpServletResponse resp) {
    Model model = null;
    try {
      model = new Model(req, resp);
      if ("/".equals(model.getRequest().getServletPath())) {
        welcome(model);
      } else {
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
      }
    } catch (Throwable e) {
      //全局异常处理
      GlobalHandler.execute(model, e);
    }
  }

  private static void welcome(Model model) {
    writer(model);
  }

  private static void writer(Model model) {
    try {
      InputStream path = GlobalHandler.class.getResourceAsStream(
          "/static/index.html");
      String content = FileUtil.readFileContent(path);
      model.getResponse().getWriter().write(content);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private static void handleFile(Model model) {
    HttpServletRequest request = model.getRequest();
    String content = getFileContentFromRequest(request);
    System.out.println(content);
  }

    public static String getFileContentFromRequest(HttpServletRequest request){
      try{
        request.setCharacterEncoding("utf-8");
        if(ServletFileUpload.isMultipartContent(request)) {
          DiskFileItemFactory diskFileItemFactory=new DiskFileItemFactory();// 创建该对象
          ServletFileUpload fileUpload=new ServletFileUpload(diskFileItemFactory);// 创建该对象
          FileItemIterator itemIterator=fileUpload.getItemIterator(request);// 解析request 请求,并返回FileItemIterator集合
          StringBuffer fileContent=new StringBuffer();
          while(itemIterator.hasNext()) {
            FileItemStream itemStream=itemIterator.next();//从集合中获得一个文件流
            if(!itemStream.isFormField() && itemStream.getName().length() > 0) {    //过滤掉表单中非文件
              BufferedInputStream inputStream=new BufferedInputStream(itemStream.openStream());   //获得文件输入流
              inputStream.mark(itemStream.openStream().available()+1);
              //判断上传的文件的编码格式 支持 UTF-8 和 ANSI两种类型
              String charsetName=getFileCharsetName(inputStream);

              BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,charsetName));
              String lineTxt=null;
              while((lineTxt=bufferedReader.readLine()) != null) {
                fileContent.append(lineTxt+"\r");
              }

            }
          }
          return fileContent.toString();
        }
      } catch(Exception e) {
        e.printStackTrace();
      }
      return null;
    }
    //判断上传的文件的编码格式 支持 UTF-8 和 ANSI两种类型
    private static String getFileCharsetName(InputStream inputStream) throws IOException{
      String charsetName="GBK";
      byte[] typeByte = new byte[3];

      inputStream.read(typeByte);
      if (typeByte[0] == -17 && typeByte[1] == -69 && typeByte[2] == -65){
        charsetName="UTF-8";
      }
      else{
        charsetName="GBK";
      }
      inputStream.reset();
      return charsetName;
    }

}
