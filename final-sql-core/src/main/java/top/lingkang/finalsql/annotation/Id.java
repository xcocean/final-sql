package top.lingkang.finalsql.annotation;

import java.lang.annotation.*;

/**
 * @author lingkang
 * Created by 2022/4/11
 * 声明列为主键ID
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
}
