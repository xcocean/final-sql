package top.lingkang.finalsql.dialect;

/**
 * @author lingkang
 * Created by 2022/4/12
 */
public class Mysql57Dialect implements SqlDialect{
    @Override
    public String queryOne() {
        return "select ? limit 1";
    }
}
