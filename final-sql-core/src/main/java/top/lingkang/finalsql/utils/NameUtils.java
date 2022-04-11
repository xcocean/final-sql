package top.lingkang.finalsql.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class NameUtils {
    private static final Pattern toHump = Pattern.compile("_+[a-zA-Z0-9]");
    private static final Pattern unHump = Pattern.compile("[a-z0-9][A-Z0-9]");

    public static String toHump(String str) {
        Matcher matcher;
        while ((matcher = toHump.matcher(str)).find()) {
            String group = matcher.group();
            str = str.replaceAll(group, group.substring(1).toUpperCase());
        }
        return str;
    }

    public static String unHump(String str) {
        str = str.substring(0, 1).toLowerCase() + str.substring(1);
        Matcher matcher;
        while ((matcher = unHump.matcher(str)).find()) {
            String group = matcher.group();
            str = str.replaceAll(group, group.substring(0, 1) + "_" + group.substring(1).toLowerCase());
        }
        return str;
    }


    public static void main(String[] args) {
        System.out.println(toHump("sys__a_user"));
        System.out.println(toHump("sys_user_role"));
        System.out.println(toHump("sys_1_role"));

        System.out.println("sysUser: " + unHump("sysUser"));
        System.out.println("sysUserRole: " + unHump("sysUserRole"));
        System.out.println("sys1Role: " + unHump("sys1Role"));
        System.out.println("sysUserRole: " + unHump("sysUserRole"));
    }
}
