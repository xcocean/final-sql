//package top.lingkang.finalsql.example.sb.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import top.lingkang.finalsql.example.sb.entity.JpaUser;
//import top.lingkang.finalsql.example.sb.entity.MyUser;
//import top.lingkang.finalsql.example.sb.mapper.JpaUserRepository;
//import top.lingkang.finalsql.example.sb.mapper.MybatisUserMapper;
//import top.lingkang.finalsql.sql.FinalSql;
//
//import java.util.Date;
//
///**
// * @author lingkang
// * Created by 2022/4/17
// * mysql 5.7.35
// * （高配电脑）amd 6核12线程， window 10 64位， DDR4 32G运行， 1T 三星 M.2固态硬盘
// * final-sql v1.0.0
// * jpa v2.1.12
// * mybatis-spring-boot-starter v2.2.2 *
// */
//@RestController
//public class TestNatureController {
//    @Autowired
//    private FinalSql finalSql;
//    @Autowired
//    private JpaUserRepository jpaUserRepository;
//    @Autowired
//    private MybatisUserMapper mybatisUserMapper;
//
//
//    @GetMapping("nature")
//    public Object nature(int type, int len) {
//        for (int i = 0; i < 2; i++) {
//            if (type == 1) {
//                finalSql(len);
//            } else if (type == 2) {
//                jpa(len);
//            } else {
//                mybatis(len);
//            }
//        }
//        return "ok";
//    }
//
//
//    private void finalSql(int len) {
//        long start = System.currentTimeMillis();
//        for (int i = 1; i <= len; i++) {
//            MyUser one = new MyUser();
//            one.setUsername("lingkang" + i);
//            one.setCreateTime(new Date());
//            one.setNum(i);
//            one.setPassword("pwd" + i);
//            finalSql.insert(one);
//        }
//        System.out.println("final-sql批量插入耗时: " + (System.currentTimeMillis() - start));
//    }
//
//    private void jpa(int len) {
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < len; i++) {
//            JpaUser one = new JpaUser();
//            one.setUsername("lingkang" + i);
//            one.setCreateTime(new Date());
//            one.setNum(i);
//            one.setPassword("pwd" + i);
//            jpaUserRepository.save(one);
//        }
//        System.out.println("Jpa批量插入耗时: " + (System.currentTimeMillis() - start));
//    }
//
//    private void mybatis(int len) {
//        long start = System.currentTimeMillis();
//        for (int i = 1; i <= len; i++) {
//            MyUser one = new MyUser();
//            one.setUsername("lingkang" + i);
//            one.setCreateTime(new Date());
//            one.setNum(i);
//            one.setPassword("pwd" + i);
//            mybatisUserMapper.insert(one);
//        }
//        System.out.println("Mybatis批量插入耗时: " + (System.currentTimeMillis() - start));
//    }
//}
