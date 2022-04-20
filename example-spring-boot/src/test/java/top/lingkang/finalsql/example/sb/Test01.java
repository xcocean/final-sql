package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.utils.ClassUtils;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test01 {
    public static void main(String[] args) {
        Integer i=0;
        Class c=Integer.class;
        System.out.println(ClassUtils.isBaseWrapper(c));
    }
}
