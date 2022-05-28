package top.lingkang.finalsql.g;

import javafx.application.Application;
import javafx.stage.Stage;
import top.lingkang.finalsql.error.FinalException;

import java.util.Map;
import java.util.Properties;

/**
 * @author lingkang
 * Created by 2022/4/17
 */
public class DbToEntityGeneratorUIMain {

    public static void main(String[] args) {
        String version = System.getProperty("java.version");
        if (!version.substring(0,2).endsWith(".")){
            throw new FinalException("java ui 不支持jdk8以上版本，jdk8以上版本已经剥离javafx");
        }
        DbToEntityGeneratorUI.main(args);
    }
}
