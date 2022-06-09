package top.lingkang.finalsql.example.sb.mysql;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/5/3
 */
public class Demo04 {
    private static List<String> stringList = null;
    public static void main(String[] args) throws Exception{
        stringList=new ArrayList<String>();
        Field stringListField = Demo04.class.getDeclaredField("stringList");
        ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
        System.out.println(stringListClass); // class java.lang.String.

    }
}
