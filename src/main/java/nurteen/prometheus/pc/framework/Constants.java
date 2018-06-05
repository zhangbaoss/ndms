package nurteen.prometheus.pc.framework;

public interface Constants {
    Integer DEFAULT_SESSION_TIMEOUT = 30; // 30分钟

    // 响应状态
    String OK = "Ok";                                   // 成功
    String FAILED = "Failed";                           // 失败
    String ERROR = "Error";                             // 错误
    String INVALID_ARGUMENT = "InvalidArgument";        // 参数无效
    String PERMISSION_DENIED = "PermissionDenied";      // 未授权
    String EXCEPTION_OCCURRED = "ExceptionOccurred";    // 发生异常
    String OTHER  = "Other";                            // 其它

    String NUID = "nuid";
    String NDID = "ndid";
    String ACCESS_TOKEN = "access-token";
    String SECRET_KEY = "secret-key";

    String WSMSGHANDLER_ONOPEN_URL = "inner$/wsmsghandler/onopen";
    String WSMSGHANDLER_ONCLOSE_URL = "inner$/wsmsghandler/onclose";
}
