package top.lingkang.finalsql.annotation;

import java.lang.annotation.*;

/**
 * @author lingkang
 * Created by 2022/5/5
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {
    String value() default "";
}
