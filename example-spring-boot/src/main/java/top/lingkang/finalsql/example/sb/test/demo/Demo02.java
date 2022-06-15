package top.lingkang.finalsql.example.sb.test.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/6/15
 */
public class Demo02 {
    public static void main(String[] args) {
        List<Integer> list=new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(1);
        list.remove(0);
        System.out.println(list);
    }
}
