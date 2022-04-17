package top.lingkang.finalsql.example.sb.entity;

import lombok.Data;
import top.lingkang.finalsql.annotation.Table;
import top.lingkang.finalsql.annotation.Id;
import top.lingkang.finalsql.annotation.Column;

import java.util.Date;

@Data
@Table
public class CustonUser {
    @Id
    @Column
    private Integer id;

    @Column
    private Integer num;

    @Column
    private String username;

    @Column
    private String password;

    @Column("create_time")
    private Date createTime;

}