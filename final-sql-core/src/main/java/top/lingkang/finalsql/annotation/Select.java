package top.lingkang.finalsql.annotation;

import java.lang.annotation.*;

/**
 * @author lingkang
 * Created by 2022/5/1
 * mapper 查询注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Select {
    /**
     * 查询语句 SQL
     */
    String value() default "";
}
