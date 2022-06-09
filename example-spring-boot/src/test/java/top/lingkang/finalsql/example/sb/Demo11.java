package top.lingkang.finalsql.example.sb;


/**
 * @author lingkang
 * Created by 2022/5/23
 */
public class Demo11 {
    public static void main(String[] args) throws Exception {
        String sql = "select (select a from b where a=1) as 'c',(select a from b where a=1) as 'c' from user where 1=1";
        int i = fromPosition(sql);
        System.out.println(sql.substring(i));
        System.out.println(i);
    }

    private static int fromPosition(String sql) {
        int i = sql.indexOf(" from ");
        String temp = sql.substring(0, i);
        int i1 = temp.indexOf("(");
        if (i1 != -1) {
            for (; ; ) {
                i = sql.indexOf(" from ", i + 1);
                i1 = sql.indexOf("(", i1 + 1);
                if (i1 == -1)
                    return i;
            }
        }
        return i;
    }


    private static int find(String sql, String str) {
        int i = sql.indexOf(str);
        if (i == -1)
            return 0;
        int count = 1;
        for (; ; ) {
            i = sql.indexOf(str, i + 1);
            if (i == -1) {
                return count;
            } else {
                count++;
            }
        }
    }
}
