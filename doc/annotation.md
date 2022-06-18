# 使用注解

### 1. 定义接口和注解

```java
import top.lingkang.finalsql.annotation.Insert;
import top.lingkang.finalsql.annotation.Select;
import top.lingkang.finalsql.annotation.Update;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/5/2
 */
public interface MyMapper {
    @Select("select id from user where id in (?)")
    Integer select(Object... obj);

    @Select
    List selectByObj(Object... obj);

    @Insert()
    Integer insert(Object... obj);

    @Update
    int update(Object... obj);

    @Update("update user set create_time=now() where id=?")
    int update(Integer id);
}
```

### 2. 实体类

```java
import lombok.Data;
import top.lingkang.finalsql.annotation.Column;
import top.lingkang.finalsql.annotation.Id;
import top.lingkang.finalsql.annotation.Table;

import java.util.Date;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
@Data
@Table("user")
public class MyUser {
    @Id
    @Column
    private Integer id;
    @Column
    private Integer num;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private Date createTime;
}
```

### 3. 调用

查询

```java
MyMapper mapper=finalSql.getMapper(MyMapper.class);
        mapper.select(1,530368);
```

插入

```java
MyMapper mapper=finalSql.getMapper(MyMapper.class);
        MyUser user=new MyUser();
        user.setUsername("lingkang");
        user.setCreateTime(new Date());
        user.setPassword("123456");
        mapper.insert(user);
        System.out.println(user);
```

更新

```java
MyMapper mapper=finalSql.getMapper(MyMapper.class);
        MyUser user=new MyUser();
        user.setUsername("lingkang123");
        user.setCreateTime(new Date());
        user.setPassword("123456123123");
        user.setId(568277);
        mapper.update(user);
        System.out.println(user);
```

## 注意事项

返回值一定要使用包装类，例如 int入参，应该使用 Integer、long返回值应该使用Long ...

## 注册为spring的bean

```java
@Bean
public MyMapper myMapper(@Qualifier("finalSql")FinalSql finalSql){
        return finalSql.getMapper(MyMapper.class);
        }

// 调用
@Autowired
private MyMapper myMapper;
```