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
public @interface Delete {
    /**
     * 删除语句SQL
     * 若删除语句SQL为空时，将会根据映射对象的 {@link Id} 作为条件进行删除
     */
    String value() default "";
}
