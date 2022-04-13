package top.lingkang.finalsql.error;

/**
 * @author lingkang
 * Created by 2022/4/13
 */
public class ConnectionException extends FinalException{
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(Throwable cause) {
        super(cause);
    }
}
