package octoveau.sso.admin.exception;

import octoveau.sso.admin.constant.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * NotFoundException
 *
 * @author yifanzheng
 */
public class NotFoundException extends AbstractThrowableProblem {
    
    private static final long serialVersionUID = -5953103061092465734L;

    public NotFoundException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.NOT_FOUND);
    }
    
}
