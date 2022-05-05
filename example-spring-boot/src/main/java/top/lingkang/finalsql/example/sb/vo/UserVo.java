package top.lingkang.finalsql.example.sb.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lingkang
 * Created by 2022/5/5
 */
@Data
public class UserVo implements Serializable {
    private Integer id;
    private String username;
    private Date createTime;
}
