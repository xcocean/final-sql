package top.lingkang.finalsql;

import cn.hutool.core.lang.Assert;
import top.lingkang.finalsql.utils.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class FinalSql {
    private DataSource dataSource;

    public FinalSql(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> query(T t) {
        Assert.notNull(t, "查询对象不能为空！");
        Connection connection = getConnection();
        //connection.prepareStatement()
        return null;
    }


    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
}
