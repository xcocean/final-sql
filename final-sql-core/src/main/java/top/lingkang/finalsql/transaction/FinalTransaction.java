package top.lingkang.finalsql.transaction;

import javax.sql.DataSource;

/**
 * @author lingkang
 * Created by 2022/4/14
 */
public class FinalTransaction {
    private DataSource dataSource;
    private boolean activity;

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
