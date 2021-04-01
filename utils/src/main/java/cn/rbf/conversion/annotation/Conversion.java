package cn.rbf.conversion.annotation;

import cn.rbf.conversion.LuckyConversion;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conversion {

    String id() default "";

    Class<? extends LuckyConversion>[] value() default LuckyConversion.class;
}
