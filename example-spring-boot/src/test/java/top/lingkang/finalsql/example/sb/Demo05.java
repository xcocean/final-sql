package top.lingkang.finalsql.example.sb;

import cn.hutool.core.io.FileUtil;
import org.beetl.core.Configuration;
import org.beetl.core.Context;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.beetl.core.statement.PlaceholderST;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/5/3
 */
public class Demo05 {
    public static void main(String[] args) throws Exception {
        URL resource = Demo05.class.getResource("/sql");
        System.out.println(resource.getPath());
        String path = resource.getPath().substring(1);
        List<String> list = FileUtil.listFileNames(path);
        System.out.println(list);

        //初始化代码
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        //获取模板
        Template t = gt.getTemplate("hello,${name} <%" +
                "if(name=='beetl'){" +
                "%>" +
                "asdsadasdas#{name}" +
                "<%}%>" +
                "<pre id=\"select\">\n" +
                "    select id,username from\n" +
                "    user where id=${id})\n" +
                "</pre>");
        t.binding("name", "beetl");
        t.binding("id", "1 or 1=1");
        List<Object> param=new ArrayList<>();
        PlaceholderST.output = new PlaceholderST.Output(){
            @Override
            public void write(Context ctx, Object value) throws IOException {
                param.add(value);
                //定制输出
                ctx.byteWriter.writeString("?");
            }
        };
        //渲染结果
        String str = t.render();
        System.out.println(str);

    }
}
