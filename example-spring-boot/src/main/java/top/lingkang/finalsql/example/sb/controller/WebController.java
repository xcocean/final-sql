package top.lingkang.finalsql.example.sb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.sql.Condition;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.transaction.FinalTransactionHolder;

import java.util.Date;
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
        List query = finalSql.select(user);
        System.out.println(query);

        MyUser one = new MyUser();
        one.setUsername("lingkang");
        System.out.println(finalSql.selectOne(one));
        System.out.println(finalSql.selectCount(new MyUser()));
        return query;
    }

    @Transactional
    @GetMapping("test")
    public Object test() {
        //System.out.println(jdbcTemplate.queryForObject("select id from user where id=1", Object.class));
        //jdbcTemplate.update("update user set username='update' where id=?", 1);
        if (1 == 1) {
            throw new RuntimeException("11");
        }
        return null;
    }

    @Transactional
    @GetMapping("insert")
    public Object insert() {
        MyUser one = new MyUser();
        one.setUsername("lingkang");
        one.setCreateTime(new Date());
        one.setNum(66);
        System.out.println(finalSql.insert(one));
        return one;
    }

    @GetMapping("select")
    public Object select() {
        MyUser user = new MyUser();
        user.setUsername("lingkang");
        List select = finalSql.select(user);
        System.out.println(finalSql.selectOne(user));
        System.out.println(finalSql.selectCount(MyUser.class));
        finalSql.select(MyUser.class,new Condition().and("username","admin").orderByAsc("id"));
        return select;
    }

    @GetMapping("one")
    public Object one() {
        System.out.println(finalSql.selectOne(new MyUser(), new Condition().orderByDesc("id").and("id", 222)));
        return finalSql.selectOne(new MyUser());
    }

    @GetMapping("count")
    public Object count() {
        return finalSql.selectCount(MyUser.class);
    }

    @Transactional
    @GetMapping("transactional")
    public Object transactional(Integer id) {
        try {
            //FinalTransactionHolder.begin();
            MyUser one = new MyUser();
            one.setUsername("transactional");
            one.setCreateTime(new Date());
            one.setNum(11111);
            System.out.println(finalSql.insert(one));
            System.out.println(finalSql.update(one));
            if (id!=1) {
                throw new RuntimeException("回滚事务");
            }
//            if (id==1)
//                FinalTransactionHolder.commit(); // 提交
        } catch (Exception e) {
            e.printStackTrace();
            //FinalTransactionHolder.rollback();// 回滚
        }
        return "ok";
    }

    @GetMapping("update")
    public Object update(){
        MyUser user=new MyUser();
        user.setId(6);
        user.setCreateTime(new Date());
        return finalSql.update(user);
    }
}
