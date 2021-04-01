package cn.rbf.config;

import java.lang.annotation.*;

/**
 * @author fk
 * @version 1.0
 * @date 2020/12/18 0018 9:47
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
    String value() default "";
}
