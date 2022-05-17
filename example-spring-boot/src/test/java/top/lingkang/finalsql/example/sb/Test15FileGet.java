package top.lingkang.finalsql.example.sb;


import top.lingkang.finalsql.sql.ResultCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/5/17
 */
public class Test15FileGet extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        Blob blob = finalSql.selectForObject("select file from files limit 1", Blob.class);
        System.out.println("size: " + blob.length());
        FileOutputStream out = new FileOutputStream("d://a.jar");
        BufferedInputStream bis = new BufferedInputStream(blob.getBinaryStream());
        byte b[] = new byte[1024];
        int len = 0;
        while ((len = bis.read(b)) != -1) {
            out.write(b, 0, len);
        }
        bis.close();
        out.close();
    }
}
