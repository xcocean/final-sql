package top.lingkang.finalsql.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author lingkang
 * Created by 2022/4/14
 */
public class FinalTransaction {
    private Connection connection;
    private boolean activity;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }
}
