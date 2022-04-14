package top.lingkang.finalsql.error;

/**
 * @author lingkang
 * Created by 2022/4/14
 */
public class TransactionException extends FinalException{
    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
