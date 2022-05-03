package top.lingkang.finalsql.annotation;

import java.lang.annotation.*;

/**
 * @author lingkang
 * Created by 2022/5/1
 * mapper 插入注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Insert {
    /**
     * 插入语句 SQL
     * 若插入语句SQL为空时，参数应该为 表映射对象！
     */
    String value() default "";
}
