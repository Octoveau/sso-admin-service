package octoveau.sso.admin.exception;

import octoveau.sso.admin.constant.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * UnauthorizedAccessException
 *
 * @author yifangzheng
 */
public class UnauthorizedAccessException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 3512785734261773194L;

    public UnauthorizedAccessException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.UNAUTHORIZED);
    }


}
