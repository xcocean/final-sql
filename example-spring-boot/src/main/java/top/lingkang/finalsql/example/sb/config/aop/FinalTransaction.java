package top.lingkang.finalsql.example.sb.config.aop;

import java.lang.annotation.*;

/**
 * @author lingkang
 * Created by 2022/6/14
 * 事务AOP
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FinalTransaction {
}

