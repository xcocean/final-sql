package top.lingkang.finalsql.g;

import cn.hutool.core.io.FileUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.dev.FinalSqlDevDataSource;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.sql.ResultCallback;
import top.lingkang.finalsql.sql.core.FinalSqlManage;
import top.lingkang.finalsql.utils.NameUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author lingkang
 * Created by 2022/4/17
 */
public class FxmlUIController implements Initializable {
    @FXML // 初始化时会自行绑定fxml里的id，注意变量命名=fxml里的id
    private TextField driver, username, password, dir, pack, prefix, prefixAdd;
    @FXML
    private TextArea url;
    @FXML
    private Button test, generator, selectDir, selectPack;

    private Connection conn;
    private String entityPath;
    private SelectGeneratorTable selectGeneratorTable = null;
    private FinalSql finalSql = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        url.setWrapText(true);
        test.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (loadDriver())
                    showAlert("连接数据库成功");
            }
        });
        dir.setText(System.getProperty("user.dir"));
        selectDir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                File file1 = new File(dir.getText());
                if (file1.exists()) {
                    chooser.setInitialDirectory(file1);
                }
                chooser.setTitle("选择打开的项目");
                File file = null;
                try {
                    file = chooser.showDialog(DbToEntityGeneratorUI.stage);
                    dir.setText(file.getPath());
                } catch (Exception e) {
                }
            }
        });

        selectPack.setOnAction(e -> {
            if ("".equals(dir.getText())) {
                showAlert("请先选择项目根目录！");
                return;
            }
            DirectoryChooser chooser = new DirectoryChooser();
            File file1 = new File(dir.getText() + File.separator + "src" + File.separator + "main");
            if (file1.exists()) {
                chooser.setInitialDirectory(file1);
            } else {
                file1 = new File(dir.getText());
                if (file1.exists())
                    chooser.setInitialDirectory(file1);
            }
            chooser.setTitle("选择打开的项目");
            File file = null;
            try {
                file = chooser.showDialog(DbToEntityGeneratorUI.stage);
                entityPath = file.getPath();
                pack.setText(entityPath.replace(dir.getText(), ""));
            } catch (Exception ee) {
            }
        });

        generator.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                finalSql = getFinalSql();
                List<String> allTable = finalSql.nativeSelect(getAllTableSql(), new ResultCallback<String>() {
                    @Override
                    public String callback(ResultSet result) throws SQLException {
                        return result.getString(1);
                    }
                });

                Button button = new Button("执行生成");
                button.setOnAction(e -> {
                    if ("".equals(dir.getText())) {
                        showAlert("根目录不能为空！");
                        return;
                    } else if ("".equals(pack.getText())) {
                        showAlert("生成包路径不能为空！");
                        return;
                    }

                    HashSet<String> select = selectGeneratorTable.getSelect();
                    System.out.println(select);
                    finalSql = getFinalSql();
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
                        if (select.contains(column.getTableName()))
                            table.add(column.getTableName());
                    }

                    for (String t : table) {
                        List<GColumn> list = new ArrayList<>();
                        for (GColumn column : columns) {
                            if (t.equals(column.getTableName())) {
                                list.add(column);
                            }
                        }
                        if (!"".equals(prefix.getText())) {
                            t = t.substring(prefix.getText().length());
                        }
                        if (!"".equals(prefixAdd.getText())) {
                            t = prefixAdd.getText() + "_" + t;
                        }
                        // 执行生成
                        File file = new File(dir.getText() + pack.getText() + File.separator +
                                NameUtils.toHump(t).substring(0, 1).toUpperCase() +
                                NameUtils.toHump(t).substring(1)
                                + ".java");
                        List<String> importHeader = new ArrayList<>();
                        String entity = "@Data\n@Table\npublic class " + NameUtils.toHump(t).substring(0, 1).toUpperCase() +
                                NameUtils.toHump(t).substring(1) + " {\n";
                        int java = pack.getText().indexOf("java");
                        if (java != -1) {
                            importHeader.add("package " + pack.getText().substring(java + 5)
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
                            String javaTypeByStr = getJavaTypeByStr(column.getType());
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
                        showAlert("生成成功！");
                    }
                });
                selectGeneratorTable = new SelectGeneratorTable(allTable, button);
            }
        });
    }

    private FinalSql getFinalSql() {
        if (finalSql == null) {
            finalSql = new FinalSqlManage(new SqlConfig(
                    new FinalSqlDevDataSource(
                            driver.getText(),
                            url.getText(),
                            username.getText(),
                            password.getText()
                    )
            ));
        }
        return finalSql;
    }

    private boolean loadDriver() {
        try {
            Class.forName(driver.getText());
            System.out.println("注册驱动成功");
            conn = DriverManager.getConnection(url.getText(), username.getText(), password.getText());
            System.out.println("链接数据库成功");
            return true;
        } catch (Exception e) {
            System.out.println("链接数据库失败");
            showAlert("错误:" + "连接数据库失败！" + e.getMessage());
            return false;
        }
    }

    private void showAlert(String content) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                new ModalDialog(content);
            }
        });
    }


    private String getAllTableSql() {
        return "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA=(select database());";
    }

    private String getTableInfoSql() {
        return "select TABLE_NAME," + // 表名 user
                "COLUMN_NAME," +// 列名称 id
                "DATA_TYPE," +// 数据类型 varchar
                "CHARACTER_MAXIMUM_LENGTH," + //数值长度 512
                "COLUMN_KEY," + // 主键 PRI
                "COLUMN_COMMENT" + // 注释
                " from information_schema.`COLUMNS`" +
                " where TABLE_SCHEMA = (select database()) order by TABLE_NAME,ORDINAL_POSITION";
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

    private String getJavaTypeByStr(String str) {
        if ("Date".equals(str)) {
            return "java.util.Date";
        }
        return null;
    }
}