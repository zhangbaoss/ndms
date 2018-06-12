package nurteen.prometheus.pc.framework.exception;

public class PermissionDeniedException extends Exception {
	private static final long serialVersionUID = 5694512284891468207L;

	public PermissionDeniedException() {
		super("操作未授权，请先进行登录认证。");
	}
}
