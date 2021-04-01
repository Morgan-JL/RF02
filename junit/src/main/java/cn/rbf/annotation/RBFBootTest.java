package cn.rbf.annotation;

import cn.rbf.core.RBFExtension;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author Morgan
 * @date 2021/4/1 14:07
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(RBFExtension.class)
public @interface RBFBootTest {

  Class<?> rootClass() default Void.class;

}
