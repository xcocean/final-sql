package top.lingkang.finalsql.sql.core;

import org.beetl.core.Context;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.statement.PlaceholderST;
import top.lingkang.finalsql.constants.ExType;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.sql.TemSqlEntity;
import top.lingkang.finalsql.utils.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/5/4
 */
public class FinalTemplate extends TemplateContentHandler {
    private GroupTemplate groupTemplate;
    private FinalSqlManage manage;

    public FinalTemplate(GroupTemplate groupTemplate, String content, FinalSqlManage manage) {
        super(content);
        this.groupTemplate = groupTemplate;
        this.manage = manage;
    }

    public GroupTemplate getGroupTemplate() {
        return groupTemplate;
    }

    public String getContent() {
        return content;
    }

    public <T> T select(String id, Class<T> returnType) {
        return select(id, returnType, null);
    }

    public <T> T select(String id, Class<T> returnType, HashMap<String, Object> param) {
        TemSqlEntity temSql = getSQL(id);
        if (temSql == null)
            throw new FinalException("不存在的模板ID：" + id);
        if (temSql.getType() != ExType.SELECT) {
            throw new FinalException("模板ID对应的SQL应该为：" + temSql.getType());
        }

        Template template = groupTemplate.getTemplate(temSql.getSql());
        List<Object> params = new ArrayList<>();
        if (param!=null){
            template.binding(param);
            PlaceholderST.output = new PlaceholderST.Output() {
                @Override
                public void write(Context ctx, Object value) throws IOException {
                    params.add(value);
                    //定制输出
                    ctx.byteWriter.writeString("?");
                }
            };
        }

        String sql = template.render();
        if (ClassUtils.isTableEntity(returnType)){
            return manage.selectForObject(sql,returnType,params);
        }



        return null;
    }

}
