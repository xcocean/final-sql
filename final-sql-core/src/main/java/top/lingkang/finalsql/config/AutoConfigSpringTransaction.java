package top.lingkang.finalsql.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import top.lingkang.finalsql.transaction.FinalSpringTransactionAutowired;
import top.lingkang.finalsql.transaction.FinalTransactionHolder;
import top.lingkang.finalsql.utils.DataSourceUtils;

/**
 * @author lingkang
 * Created by 2022/4/15
 * 自动配置事务到spring的 @Transactional 注解上
 */
public class AutoConfigSpringTransaction {
    private static final Logger log = LoggerFactory.getLogger(AutoConfigSpringTransaction.class);

    public AutoConfigSpringTransaction() {
        log.info("final-sql：检测到spring项目，自动将事务交给spring托管，受 @Transactional 控制事务");
        DataSourceUtils.setSpringTx(new FinalSpringTransactionAutowired() {
            @Override
            public void register() {
                if (TransactionSynchronizationManager.isSynchronizationActive()) {
                    if (FinalTransactionHolder.isOpen()) {// 已经开启事务
                        return;
                    }
                    FinalTransactionHolder.begin();// 开启事务
                    // 注册事务管理
                    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status == 0) {// 提交事务
                                FinalTransactionHolder.commit();
                            } else {// 回滚事务
                                FinalTransactionHolder.rollback();
                            }
                        }
                    });
                }
            }
        });
    }
}
