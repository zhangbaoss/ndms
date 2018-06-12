package nurteen.prometheus.pc.framework;

import com.sun.java.swing.plaf.windows.resources.windows;

public interface Constants {
    Integer DEFAULT_SESSION_TIMEOUT = 30 * 60; // 30分钟

    // 响应状态
    String OK = "Ok";                                   // 成功
    String FAILED = "Failed";                           // 失败
    String ERROR = "Error";                             // 错误
    String INVALID_ARGUMENT = "InvalidArgument";        // 参数无效
    String PERMISSION_DENIED = "PermissionDenied";      // 未授权
    String EXCEPTION_OCCURRED = "ExceptionOccurred";    // 发生异常
    String OTHER  = "Other";                            // 其它

    String WXALOGIN_BEGIN_SALT = "wxalogin$begin$salt";
    String WXALOGIN_END_SALT = "wxalogin$end$salt";

    String NUID = "nuid";
    String NDID = "ndid";
    String LOGIN_TIME = "login-time";
    String CONNECT_TIME = "connect-time";
    String ACCESS_TOKEN = "access-token";
    String SECRET_KEY = "secret-key";

    String WSMSGHANDLER_ONOPEN_URL = "inner$/wsmsghandler/onopen";
    String WSMSGHANDLER_ONCLOSE_URL = "inner$/wsmsghandler/onclose";

    // Device Type
    int DEVICE_TYPE_APP_BROWSER = 1;
    int DEVICE_TYPE_APP_MOBILE = 2;
    int DEVICE_TYPE_APP_PC = 3;
    int DEVICE_TYPE_PC_CONTROLLER = 4;
    int DEVICE_TYPE_PC_CENTER = 5;

    // Device Platform
    int DEVICE_PLATFORM_WINDOWS = 1;
    int DEVICE_PLATFORM_LINUX = 2;
    int DEVICE_PLATFORM_UNIX = 3;
    int DEVICE_PLATFORM_BROWSER = 4;
    int DEVICE_PLATFORM_IOS = 5;
    int DEVICE_PLATFORM_ANDROID = 6;

}
