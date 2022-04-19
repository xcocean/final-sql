package top.lingkang.finalsql.g;

import cn.hutool.core.io.FileUtil;
import top.lingkang.finalsql.constants.DbType;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.sql.ResultCallback;
import top.lingkang.finalsql.utils.DataSourceUtils;
import top.lingkang.finalsql.utils.NameUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class GGeneratorManage {

    private GGeneratorConfig config;
    private FinalSql finalSql;
    private DbType dbType;

    public GGeneratorManage(GGeneratorConfig config, FinalSql finalSql) {
        this.config = config;
        this.finalSql = finalSql;
        this.dbType = DataSourceUtils.getDataType(finalSql.getDataSource());
    }

    public void build() {
        File path = new File(config.getGeneratorPath());
        if (!path.exists()) {
            throw new FinalException("路径不存在: " + config.getGeneratorPath());
        }
        if (config.getGeneratorPath().endsWith(File.separator) ||
                config.getGeneratorPath().endsWith("\\/")) {
            config.setGeneratorPath(config.getGeneratorPath().substring(0, config.getGeneratorPath().length() - 1));
        } else if (config.getGeneratorPath().endsWith("//")) {
            config.setGeneratorPath(config.getGeneratorPath().substring(0, config.getGeneratorPath().length() - 2));
        }

        List<GColumn> columns = finalSql.nativeSelect(getTableInfoSql(), new ResultCallback<GColumn>() {
            @Override
            public GColumn callback(ResultSet result) throws SQLException {
                return new GColumn(
                        result.getString(1),
                        result.getString(2),
                        getJavaType(result.getString(3)),
                        result.getString(4),
                        result.getString(5).indexOf("PRI") != -1,
                        result.getString(6)

                );
            }
        });
        System.out.println(columns);
        Set<String> table = new HashSet<>();
        for (GColumn column : columns) {
            if (config.getGeneratorTables().contains(column.getTableName()))
                table.add(column.getTableName());
        }

        for (String t : table) {
            List<GColumn> list = new ArrayList<>();
            for (GColumn column : columns) {
                if (t.equals(column.getTableName())) {
                    list.add(column);
                }
            }
            if (!"".equals(config.getIgnorePrefix())) {
                t = t.substring(config.getIgnorePrefix().length());
            }
            if (!"".equals(config.getAddPrefix())) {
                t = config.getAddPrefix() + "_" + t;
            }
            // 执行生成
            File file = new File(config.getGeneratorPath() + File.separator +
                    NameUtils.toHump(t).substring(0, 1).toUpperCase() +
                    NameUtils.toHump(t).substring(1)
                    + ".java");
            List<String> importHeader = new ArrayList<>();
            String entity = "@Data\n@Table\npublic class " + NameUtils.toHump(t).substring(0, 1).toUpperCase() +
                    NameUtils.toHump(t).substring(1) + " {\n";
            int java = config.getGeneratorPath().indexOf("java");
            if (java != -1) {
                importHeader.add("package " + config.getGeneratorPath().substring(java + 5)
                        .replaceAll("\\/", ".")
                        .replaceAll("\\\\", ".")
                        + ";\n\n");
            }
            importHeader.add("import lombok.Data;\n");
            importHeader.add("import top.lingkang.finalsql.annotation.Table;\n");
            importHeader.add("import top.lingkang.finalsql.annotation.Id;\n");
            importHeader.add("import top.lingkang.finalsql.annotation.Column;\n\n");
            for (GColumn column : list) {
                if (column.isPri()) {
                    entity += "    @Id\n";
                }
                entity += "    @Column" + (column.getName().equals(NameUtils.toHump(column.getName())) ? "\n" : "(\"" + column.getName() + "\")\n");
                entity += "    private " + column.getType() + " " + NameUtils.toHump(column.getName()) + ";\n\n";
                String javaTypeByStr = getJavaTypeImport(column.getType());
                if (javaTypeByStr != null && !importHeader.contains(javaTypeByStr))
                    importHeader.add("import " + javaTypeByStr + ";\n");
            }
            String imp = "";
            for (String s : importHeader) {
                imp += s;
            }
            entity = imp + "\n" + entity;
            entity += "}";
            System.out.println(entity);
            FileUtil.writeString(entity, file, Charset.forName("UTF-8"));
            System.out.println("生成成功！");
        }

    }


    /**
     * https://www.cnblogs.com/hwaggLee/p/5111019.html
     */
    private String getJavaType(String type) {
        type = type.toLowerCase();
        if (type.indexOf("char") != -1) {
            return "String";
        } else if (type.indexOf("time") != -1) {
            return "Date";
        } else if (type.indexOf("int") != -1) {
            return "Integer";
        } else if (type.indexOf("long") != -1) {
            return "Long";
        } else if (type.indexOf("blob") != -1) {
            return "byte[]";
        } else if (type.indexOf("bit") != -1) {
            return "Boolean";
        } else if (type.indexOf("float") != -1) {
            return "Float";
        } else if (type.indexOf("double") != -1) {
            return "Double";
        } else {
            return null;
        }
    }

    private String getJavaTypeImport(String str) {
        if ("Date".equals(str)) {
            return "java.util.Date";
        }
        return null;
    }


    /**
     * 所有表名
     *
     * @return
     */
    private String getAllTableSql() {
        if (dbType == DbType.MYSQL) {
            return "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA=(select database());";
        }else if (dbType==DbType.POSTGRESQL){
            return "select tablename from pg_tables where schemaname = 'public';";
        }
        return "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA=(select database());";
    }

    /**
     * 表名
     * 列名称
     * 列类型
     * 列数据长度
     * 主键
     * 注释
     *
     * @return
     */
    private String getTableInfoSql() {
        if (dbType == DbType.MYSQL) {
            return "select TABLE_NAME," + // 表名 user
                    "COLUMN_NAME," +// 列名称 id
                    "DATA_TYPE," +// 数据类型 varchar
                    "CHARACTER_MAXIMUM_LENGTH," + //数值长度 512
                    "COLUMN_KEY," + // 主键 PRI
                    "COLUMN_COMMENT" + // 注释
                    " from information_schema.`COLUMNS`" +
                    " where TABLE_SCHEMA = (select database()) order by TABLE_NAME,ORDINAL_POSITION";
        }


        return "select TABLE_NAME," + // 表名 user
                "COLUMN_NAME," +// 列名称 id
                "DATA_TYPE," +// 数据类型 varchar
                "CHARACTER_MAXIMUM_LENGTH," + //数值长度 512
                "COLUMN_KEY," + // 主键 PRI
                "COLUMN_COMMENT" + // 注释
                " from information_schema.`COLUMNS`" +
                " where TABLE_SCHEMA = (select database()) order by TABLE_NAME,ORDINAL_POSITION";
    }

}
