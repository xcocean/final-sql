package top.lingkang.finalsql.annotation;

import top.lingkang.finalsql.base.IdGenerate;
import top.lingkang.finalsql.constants.IdType;

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
    /**
     * 自动生成 ID 默认针对mysql的自动增长，若是手动添加id，则应该将类型改为 IdType.INPUT
     */
    IdType value() default IdType.AUTO;

    /**
     * sequence 是针对 postgre SQL 等数据库设计。若是MySQL需要设置自增长
     */
    String sequence() default "";
}
