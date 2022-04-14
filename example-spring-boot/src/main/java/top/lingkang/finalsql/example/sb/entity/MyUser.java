package top.lingkang.finalsql.example.sb.entity;

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
    //@Column
    private String password;
    @Column
    private Date createTime;
}
