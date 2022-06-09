package top.lingkang.finalsql.example.sb.test.mysql;


/**
 * @author lingkang
 * Created by 2022/5/3
 */
public class Demo06 {
    public static void main(String[] args) {
        String html="<html>\n" +
                "    <title>hello</title>\n" +
                "  <head>\n" +
                "  </head>\n" +
                "   \n" +
                "  <body>\n" +
                "      <form action=\"/FileUpload/Upload\" method=\"post\" enctype=\"multipart/form-data\">\n" +
                "          <input name=\"name\"/>\n" +
                "          <input type=\"file\" name=\"head\"/>\n" +
                "          <input type=\"submit\" value=\"upload\"/>\n" +
                "      </form>\n" +
                "  </body>\n" +
                "</html>\n";
        int i = html.indexOf("<input",160);
        System.out.println(i);
        System.out.println(html.substring(i));


    }
}
