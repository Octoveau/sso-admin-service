package octoveau.sso.admin.exception;

/**
 * ServiceException
 *
 * @author yifanzheng
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -8773139510494001347L;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}
	
}