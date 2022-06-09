package top.lingkang.finalsql.utils;

import top.lingkang.finalsql.constants.DbType;
import top.lingkang.finalsql.error.FinalException;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class DataSourceUtils {

    public static DbType getDataType(DataSource dataSource) throws FinalException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String name = connection.getMetaData().getDriverName();
            if (CommonUtils.isEmpty(name)) {
                throw new FinalException("配置方言失败：未识别的jdbc连接驱动");
            }
            name = name.toLowerCase();
            if (name.indexOf("mysql") != -1) {
                return DbType.MYSQL;
            } else if (name.indexOf("postgresql") != -1) {
                return DbType.POSTGRESQL;
            }
        } catch (Exception e) {
            throw new FinalException(e);
        } finally {
            CommonUtils.close(connection);
        }
        return DbType.OTHER;
    }

}
