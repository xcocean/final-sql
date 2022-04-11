package top.lingkang.finalsql;

/**
 * @author lingkang
 * Created by 2022/4/12
 */
public class SqlConfig {
    private boolean showSqlLog=false;
    private boolean showResultLog=false;


    public boolean isShowSqlLog() {
        return showSqlLog;
    }

    public void setShowSqlLog(boolean showSqlLog) {
        this.showSqlLog = showSqlLog;
    }

    public boolean isShowResultLog() {
        return showResultLog;
    }

    public void setShowResultLog(boolean showResultLog) {
        this.showResultLog = showResultLog;
    }
}
