package top.lingkang.finalsql.example.sb.test.demo;

/**
 * @author lingkang
 * Created by 2022/6/10
 */
public class Demo01 {
    public static void main(String[] args) {
        String sql = "" +
                "select id,(id/2),(select count(*) from user),(select id from user),(id/2) as cid from a where id in (select id from uu)";

        System.out.println(total(sql));
    }

    public static String total(String sql) {
        String low = sql.toLowerCase();
        int from = low.indexOf(" from ");
        String temp = sql.substring(0, from);
        int i1 = temp.indexOf("(");
        if (i1 != -1) {
            do {
                int select = low.indexOf("select", i1);
                if (select != -1 && select < from) {
                    from = low.indexOf(" from ", from + 1);
                    i1 = select;
                }
                i1 = low.indexOf("(", i1 + 1);
            } while (i1 != -1);
        }
        i1 = low.indexOf("order");
        if (i1 != -1) {
            return "select count(*)" + sql.substring(from, i1);
        }
        return "select count(*)" + sql.substring(from);
    }

    private static int findSelect(String sql, int start, int from) {
        int select = sql.indexOf("select", start);
        if (select != -1 && select < from) {
            from = sql.indexOf(" from ", from + 1);
            int select1 = findSelect(sql, from, from);
            if (select != -1) {

            }
        } else
            return -1;
        return select;
    }
}
