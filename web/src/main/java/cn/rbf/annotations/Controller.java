package cn.rbf.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Morgan
 * @date 2020/12/3 8:54
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Await
@Component(type = "controller")
public @interface Controller {

  /**
   * 组件ID
   */
  String value() default "";

  /**
   * 路由地址：可以设置一个路由的代称[如：User服务->{@code url="/user"}]
   */
  String url() default "/";

}
