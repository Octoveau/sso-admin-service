package octoveau.sso.admin.exception;

/**
 * 主要用于将InterruptedException转换为非受检的异常
 *
 * @author yifanzheng
 */
public class ThreadInterruptedException extends RuntimeException {

    public ThreadInterruptedException(InterruptedException exception) {
        super(exception);
    }

}
