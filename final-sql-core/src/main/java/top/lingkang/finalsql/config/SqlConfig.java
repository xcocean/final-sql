package top.lingkang.finalsql.config;

import top.lingkang.finalsql.base.SqlInterceptor;
import top.lingkang.finalsql.dialect.SqlDialect;

import javax.sql.DataSource;

/**
 * @author lingkang
 * Created by 2022/4/12
 * 配置中的 DataSource 应该为连接池 DataSource
 */
public class SqlConfig {
    public SqlConfig() {
    }

    /**
     * 配置中的 DataSource 应该为连接池 DataSource
     */
    public SqlConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // ------ 基本连接设置 --------------------------------------------------------
    private DataSource dataSource;

    // 日志显示
    private boolean showLog = false;

    // 将此语句对象生成的任何ResultSet对象可以包含的最大行数设置为给定数目。如果超过限制，多余的行将被自动删除。
    // 默认为 0 不限制
    private int maxRows = 0;

    // 该参数的目的是为了减少网络交互次数设计的。在访问 ResultSet时，如果它每次只从服务器上读取一行数据，会产生大量开销。
    // FetchSize 参数的作用是 在调用 rs.next时， ResultSet会一次性从服务器上取多少行数据回来。这样在下次 rs.next 时，
    // 他可以直接从内存中获取数据而不需要网络交互，提高了效率。但是这个设置可能会被某些jdbc驱动忽略。设置过大也会造成内存上升。
    // 默认为 0
    private int fetchSize = 0;


    // ------- 定制设置  --------------------------------------------------------
    private SqlDialect sqlDialect;
    private SqlInterceptor[] interceptor = null;


    // --------- get set -------------------------------------------------------


    public SqlInterceptor[] getInterceptor() {
        return interceptor;
    }

    public SqlConfig setInterceptor(SqlInterceptor[] interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public SqlConfig setMaxRows(int maxRows) {
        this.maxRows = maxRows;
        return this;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public SqlConfig setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public SqlConfig setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public SqlDialect getSqlDialect() {
        return sqlDialect;
    }

    public SqlConfig setSqlDialect(SqlDialect sqlDialect) {
        this.sqlDialect = sqlDialect;
        return this;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public SqlConfig setShowLog(boolean showLog) {
        this.showLog = showLog;
        return this;
    }
}
