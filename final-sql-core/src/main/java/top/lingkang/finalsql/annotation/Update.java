package top.lingkang.finalsql.annotation;

import java.lang.annotation.*;

/**
 * @author lingkang
 * Created by 2022/5/1
 * mapper 更新注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Update {
    /**
     * 更新语句SQL
     * 若更新语句SQL为空时，将会根据映射对象的 {@link Id} 作为条件进行更新
     */
    String value() default "";
}
