package top.lingkang.finalsql.g;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class GGeneratorConfig {
    private DataSource dataSource;

    /**
     * 生成的路径
     */
    private String generatorPath;

    /**
     * 需要生成的表
     */
    private List<String> generatorTables;

    /**
     * 需要忽略的数据库前缀
     */
    private String ignorePrefix;

    /**
     * 需要添加的前缀
     */
    private String addPrefix;

    // ---------------------------------------------


    public String getGeneratorPath() {
        return generatorPath;
    }

    public void setGeneratorPath(String generatorPath) {
        this.generatorPath = generatorPath;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> getGeneratorTables() {
        return generatorTables;
    }

    public void setGeneratorTables(List<String> generatorTables) {
        this.generatorTables = generatorTables;
    }

    public String getIgnorePrefix() {
        return ignorePrefix;
    }

    public void setIgnorePrefix(String ignorePrefix) {
        this.ignorePrefix = ignorePrefix;
    }

    public String getAddPrefix() {
        return addPrefix;
    }

    public void setAddPrefix(String addPrefix) {
        this.addPrefix = addPrefix;
    }
}
