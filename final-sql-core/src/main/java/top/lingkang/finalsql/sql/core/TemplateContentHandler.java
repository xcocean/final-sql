package top.lingkang.finalsql.sql.core;

import cn.hutool.core.util.StrUtil;
import top.lingkang.finalsql.constants.ExType;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.sql.TemSqlEntity;

import java.util.HashMap;

/**
 * @author lingkang
 * Created by 2022/5/4
 */
public abstract class TemplateContentHandler {
    protected String content;
    private HashMap<String, TemSqlEntity> map = new HashMap<>();

    public TemplateContentHandler(String content) {
        this.content = content;
        init();
    }

    private void init() {
        if (StrUtil.isEmpty(content))
            return;
        int position = 0, start = -1, end = -1;
        while (true) {
            start = content.indexOf("<pre", position);
            if (start == -1)
                return;
            end = content.indexOf("</pre>", position);
            if (end == -1)
                throw new FinalException("解析SQL模板异常，未找到 </pre> 结束标签，start position: " + content.substring(start, 6));

            String sql = content.substring(start, end);
            int left = sql.indexOf("\"");
            int right = sql.indexOf("\"", left + 1);
            String id = sql.substring(left + 1, right);
            int i1 = sql.indexOf(">");
            sql = sql.substring(i1 + 1);
            System.out.println(id);
            System.out.println(sql.trim());
            if (map.containsKey(id)) {
                throw new FinalException("存在重复的id：" + id);
            }
            map.put(id, new TemSqlEntity(sql, getType(sql, id)));
            position = end;
        }
    }

    private ExType getType(String sql, String id) {
        String tem = sql.toLowerCase();
        if (tem.contains("select")) {
            return ExType.SELECT;
        } else if (tem.contains("update")) {
            return ExType.UPDATE;
        } else if (tem.contains("insert")) {
            return ExType.INSERT;
        } else if (tem.contains("delete")) {
            return ExType.DELETE;
        }
        throw new FinalException("解析模板SQL异常, id=" + id + " sql: " + sql);
    }

    protected TemSqlEntity getSQL(String id) {
        return map.get(id);
    }
}
