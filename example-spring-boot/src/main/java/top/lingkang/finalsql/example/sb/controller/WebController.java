package top.lingkang.finalsql.example.sb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lingkang.finalsql.FinalSql;
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
    @Autowired
    private FinalSql finalSql;

    @GetMapping("")
    public Object index() {
        MyUser user = new MyUser();
        List query = finalSql.query(user);
        System.out.println(query);

        MyUser one = new MyUser();
        one.setUsername("lingkang");
        System.out.println(finalSql.queryOne(one));
        System.out.println(finalSql.queryCount(new MyUser()));
        return query;
    }

    @Transactional
    @GetMapping("test")
    public Object test() {
        System.out.println(jdbcTemplate.queryForObject("select id from user where id=1", Object.class));
        System.out.println(jdbcTemplate.queryForObject("select id from user where id=1", Object.class));
        System.out.println(jdbcTemplate.queryForObject("select id from user where id=1", Object.class));
        return null;
    }
}
