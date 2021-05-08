package cn.rbf.initializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public interface WebApplicationInitializer {

    void onStartup(ServletContext servletContext) throws ServletException;

    default double priority(){
        return 5;
    }
}
