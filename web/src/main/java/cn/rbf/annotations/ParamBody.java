package cn.rbf.annotations;

/**
 * @author cage
 * @date 2020/12/4 15:55
 */
import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamBody {
    boolean required() default true;
}

