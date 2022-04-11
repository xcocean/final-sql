package top.lingkang.finalsql.annotation;

import java.lang.annotation.*;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Nullable {
}
