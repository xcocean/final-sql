package top.lingkang.finalsql.test;

import top.lingkang.finalsql.annotation.Column;
import top.lingkang.finalsql.annotation.Table;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
@Table("t_test")
public class TestEnity {
    @Column
    private int id;
}
