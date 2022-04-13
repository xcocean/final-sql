package top.lingkang.finalsql.error;

/**
 * @author lingkang
 * Created by 2022/4/13
 */
public class FinalException extends RuntimeException{
    public FinalException(String message) {
        super(message);
    }

    public FinalException(String message, Throwable cause) {
        super(message, cause);
    }

    public FinalException(Throwable cause) {
        super(cause);
    }
}
