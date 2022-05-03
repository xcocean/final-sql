package top.lingkang.finalsql.example.sb;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.example.sb.mapper.MyMapper;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test14Mapper extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        MyMapper mapper = finalSql.getMapper(MyMapper.class);
        mapper.select(1,530368);
    }
}
