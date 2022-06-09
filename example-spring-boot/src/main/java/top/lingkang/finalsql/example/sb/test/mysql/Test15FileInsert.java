package top.lingkang.finalsql.example.sb.test.mysql;


import java.io.File;
import java.io.FileInputStream;

/**
 * @author lingkang
 * Created by 2022/5/17
 */
public class Test15FileInsert extends TestBase {
    public static void main(String[] args) throws Exception {

        File file = new File("C:\\Users\\Administrator\\Desktop\\jd-gui-1.6.6-min.jar");
        FileInputStream fin = new FileInputStream(file);
        byte[] ib = new byte[(int) file.length()];
        fin.read(ib);
        fin.close();
        int i = finalSql.nativeUpdate("insert into files(file) values(?)", ib);
        System.out.println(i);
    }
}
