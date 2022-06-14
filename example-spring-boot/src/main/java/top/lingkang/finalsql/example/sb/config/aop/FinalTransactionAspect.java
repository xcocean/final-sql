package top.lingkang.finalsql.example.sb.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.lingkang.finalsql.sql.FinalSql;

/**
 * @author lingkang
 * Created by 2022/6/14
 */
@Aspect
@Component
public class FinalTransactionAspect {
    @Autowired
    private FinalSql finalSql;

    /**
     * 切入点
     */
    @Pointcut("@annotation(top.lingkang.finalsql.example.sb.config.aop.FinalTransaction)")
    public void annotationPointcut() {
    }

    @Around("annotationPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            finalSql.beginTransaction();// 开始事务
            Object proceed = joinPoint.proceed();
            finalSql.commitTransaction();// 提交事务
            return proceed;
        } catch (Throwable throwable) {
            finalSql.rollbackTransaction();// 回滚事务
            throw throwable;// 直接抛出异常
        }
    }
}
