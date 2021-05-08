package cn.rbf.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author morgan
 * @date 2020/12/4 9:42
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Plugin
@Component(type = "http")
public @interface Http {

  /**
   * 组件名称
   */
  String value() default "";

  /**
   * 服务调用http -> [{@code url="http:ip:port"}]
   */
  String url() default "";

  /**
   * 从配置文件中读取key
   */
  String key() default "";
}
