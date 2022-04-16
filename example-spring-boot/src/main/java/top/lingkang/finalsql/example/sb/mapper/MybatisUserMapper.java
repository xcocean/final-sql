//package top.lingkang.finalsql.example.sb.mapper;
//
//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Options;
//import top.lingkang.finalsql.example.sb.entity.MyUser;
//
//import java.util.Date;
//
///**
// * @author lingkang
// * Created by 2022/4/17
// */
//public interface MybatisUserMapper {
//    @Insert("insert into user(num,username,password,create_time) values (#{num},#{username},#{password},#{createTime})")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
//    int insert(MyUser user);
//}
