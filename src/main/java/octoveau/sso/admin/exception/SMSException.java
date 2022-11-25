package octoveau.sso.admin.exception;

import octoveau.sso.admin.constant.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;

/**
 * SMSException
 * 
 * @author yifanzheng
 */
public class SMSException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -1996994576593823810L;

    public SMSException(String message, StatusType status) {
        super(ErrorConstants.DEFAULT_TYPE, message, status);
    }
}
