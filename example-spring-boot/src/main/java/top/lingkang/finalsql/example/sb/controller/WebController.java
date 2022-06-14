package top.lingkang.finalsql.example.sb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.example.sb.mapper.MybatisUserMapper;
import top.lingkang.finalsql.sql.Condition;
import top.lingkang.finalsql.sql.FinalSql;

import java.sql.*;
import java.util.ArrayList;
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
    @Autowired
    private MybatisUserMapper mybatisUserMapper;

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
        jdbcTemplate.queryForObject("", MyUser.class);
        if (1 == 1) {
            throw new RuntimeException("11");
        }
        return null;
    }

    @GetMapping("insert")
    public Object insert() {
        MyUser one = new MyUser();
        one.setUsername("lingkang");
        one.setCreateTime(new Date());
        one.setNum(66);
        finalSql.beginTransaction();
        System.out.println(finalSql.insert(one));
        finalSql.commitTransaction();
        return one;
    }

    @GetMapping("select")
    public Object select() {
        MyUser user = new MyUser();
        user.setUsername("lingkang");
        List select = finalSql.select(user);
        System.out.println(finalSql.selectOne(user));
        System.out.println(finalSql.selectCount(MyUser.class));
        finalSql.select(MyUser.class, new Condition().eq("username", "admin").orderByAsc("id"));

        System.out.println("----------" + finalSql.select(MyUser.class, new Condition().gt("id", 6)));
        System.out.println("######" + finalSql.select(MyUser.class, new Condition().custom("and id=? and create_time<now()", 4)));
        return select;
    }

    @GetMapping("one")
    public Object one() {
        System.out.println(finalSql.selectOne(new MyUser(), new Condition().orderByDesc("id").eq("id", 222)));
        return finalSql.selectOne(new MyUser());
    }

    @GetMapping("count")
    public Object count() {
        List<Integer> in = new ArrayList<>();
        in.add(1);
        in.add(5);
        return finalSql.selectCount(MyUser.class, new Condition().andIn("id", in));
    }

    @GetMapping("transactional")
    public Object transactional(Integer id) {
        try {
            finalSql.beginTransaction();

            MyUser one = new MyUser();
            one.setUsername("transactional");
            one.setCreateTime(new Date());
            one.setNum(11111);
            System.out.println(finalSql.insert(one));
            System.out.println(finalSql.update(one));
            if (id != 1) {
                throw new RuntimeException("回滚事务");
            }
            finalSql.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            // 回滚
            finalSql.rollbackTransaction();
        }
        return "ok";
    }

    @Transactional
    @GetMapping("tr")
    public Object tr() {
        finalSql.nativeUpdate("update user set num=? where id=?", 66, 6);
        if (1 == 1)
            throw new FinalException("1");
        return "ok";
    }

    @GetMapping("tra")
    public Object tra() {
        finalSql.beginTransaction();
        finalSql.beginTransaction();
        return "ok";
    }

    @GetMapping("update")
    public Object update() {
        MyUser user = new MyUser();
        user.setId(6);
        user.setCreateTime(new Date());
        return finalSql.update(user);
    }

    @GetMapping("delete")
    public Object delete(Integer id) {
        /*if (id != null) {
            return finalSql.delete(MyUser.class, new Condition().eq("id", id));
        }*/
        MyUser user = new MyUser();
        user.setId(5);
        System.out.println(finalSql.delete(user));
        return 0;
    }

    @GetMapping("like")
    public Object like() {
        System.out.println(finalSql.select(MyUser.class, new Condition().like("username", "gk")));
        System.out.println(finalSql.select(MyUser.class, new Condition().rightLike("username", "ling")));
        System.out.println(finalSql.select(MyUser.class, new Condition().leftLike("username", "ling")));
        return 1;
    }

    /*@GetMapping("batchInsert")
    public Object batchInsert() {
        int result = 0;
        long start = System.currentTimeMillis();
        MyUser one = new MyUser();
        for (int i = 1; i <= 2000; i++) {
            one.setUsername("lingkang" + i);
            one.setCreateTime(new Date());
            one.setNum(i);
            //one.setId(i);
            one.setPassword("pwd" + i);
            finalSql.insert(one);
            result++;
        }
        System.out.println("final-sql批量插入耗时: " + (System.currentTimeMillis() - start));
        return result;
    }*/

    @GetMapping("testInsert")
    public Object testInsert() {
        for (int i = 1; i <= 20; i++) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int finalI = i;
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection conn)
                        throws SQLException {
                    // 预处理
                    PreparedStatement ps = conn.prepareStatement(
                            "insert into user(num,username,password,create_time) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setInt(1, finalI);
                    ps.setString(2, "lingkang" + finalI);
                    ps.setString(3, "pwd" + finalI);
                    ps.setObject(4, new Date());
                    return ps;
                }
            }, keyHolder);
            // 返回主键
            System.out.println("插入后数据主键：" + keyHolder.getKey().intValue());
        }
        return 1;
    }

    @GetMapping("batchInsert")
    public Object batchInsert() {
        List<MyUser> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyUser user = new MyUser();
            user.setUsername("lingkang" + i);
            user.setCreateTime(new Date());
            list.add(user);
        }
        return finalSql.batchInsert(list);
    }

    @GetMapping("m")
    public Object mybatis() {
        System.out.println(mybatisUserMapper.getClass());
        return mybatisUserMapper.getClass();
    }
}
