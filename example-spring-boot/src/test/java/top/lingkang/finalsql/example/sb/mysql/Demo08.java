package top.lingkang.finalsql.example.sb.mysql;

/**
 * @author lingkang
 * Created by 2022/5/9
 */
public class Demo08 {
    public static void main(String[] args) {
        a();
    }

    private static void a(){
        Thread thread = Thread.currentThread();
        String className = Thread.currentThread().getStackTrace()[2].getClassName();//调用的类名
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();//调用的方法名
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();//调用的行数
        System.out.println(thread.getStackTrace()[1]);
        System.out.println(thread.getStackTrace()[2]);
        System.out.println(thread.getStackTrace()[3]);
    }
}
