package top.lingkang.finalsql.example.sb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lingkang.finalsql.FinalSql;
import top.lingkang.finalsql.SqlConfig;
import top.lingkang.finalsql.example.sb.entity.MyUser;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
@RestController
public class WebController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("")
    public Object index() {
        SqlConfig config=new SqlConfig();
        config.setShowSqlLog(true);
        config.setShowResultLog(true);
        FinalSql finalSql = new FinalSql(jdbcTemplate.getDataSource(),config);
        List query = finalSql.query(MyUser.class);
        System.out.println(query);
        return query;
    }
}
