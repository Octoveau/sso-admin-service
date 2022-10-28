package octoveau.sso.admin.exception;

import octoveau.sso.admin.constant.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * AlreadyExistsException
 *
 * @author yifanzheng
 */
public class AlreadyExistsException extends AbstractThrowableProblem {
    
    private static final long serialVersionUID = -8199209035141252092L;

    public AlreadyExistsException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.CONFLICT);
    }
}
