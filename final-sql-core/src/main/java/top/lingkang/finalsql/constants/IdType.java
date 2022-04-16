package top.lingkang.finalsql.constants;


/**
 * @author lingkang
 * Created by 2022/4/17
 */
public enum IdType {
    /**
     * 自动id生成策略，需要数据库支持，并设置自动增长或其他
     */
    AUTO,

    /**
     * 持久化时必须分配Id，否则将抛出Id为空异常！
     */
    INPUT,
}
