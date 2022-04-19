package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.dev.FinalSqlDevDataSource;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.sql.core.FinalSqlManage;

import javax.activation.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class TestPostgresql01 {
    public static void main(String[] args) throws Exception{
        FinalSqlDevDataSource dataSource=new FinalSqlDevDataSource(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test?serverTimezone=UTC",
                "root",
                "123456"
        );
        SqlConfig sqlConfig = new SqlConfig(dataSource);
        FinalSql finalSql = new FinalSqlManage(sqlConfig);
        Connection connection = finalSql.getDataSource().getConnection();
        String schema = connection.getSchema();
        System.out.println(schema);
        DatabaseMetaData metaData = connection.getMetaData();
        System.out.println(metaData.getSchemas());
    }
}
