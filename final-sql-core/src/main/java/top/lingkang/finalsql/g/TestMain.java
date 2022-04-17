package top.lingkang.finalsql.g;

import java.util.Properties;

/**
 * @author lingkang
 * Created by 2022/4/17
 */
public class TestMain {
    public static void main(String[] args) {
        Properties properties = System.getProperties();
        for(String key : properties.stringPropertyNames()){
            String value = properties.getProperty(key);
            System.out.println(key+": " + value);
        }
    }
}
