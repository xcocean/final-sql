package top.lingkang.finalsql.example.sb;

/**
 * @author lingkang
 * Created by 2022/4/27
 */
public class Demo02 {
    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(e.getMessage());
            }
        });
        if (1==1){
            //throw new RuntimeException("123");
        }
     }
}
