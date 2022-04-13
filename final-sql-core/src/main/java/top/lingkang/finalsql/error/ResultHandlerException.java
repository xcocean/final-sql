package top.lingkang.finalsql.error;

/**
 * @author lingkang
 * Created by 2022/4/13
 */
public class ResultHandlerException extends FinalException{
    public ResultHandlerException(String message) {
        super(message);
    }

    public ResultHandlerException(Throwable cause) {
        super(cause);
    }
}
