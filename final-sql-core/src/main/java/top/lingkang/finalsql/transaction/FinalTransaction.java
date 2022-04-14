package top.lingkang.finalsql.transaction;

import java.sql.Connection;

/**
 * @author lingkang
 * Created by 2022/4/14
 */
public class FinalTransaction {
    private Connection connection;
    private boolean activity;
    private long threadId;

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

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
