package nurteen.prometheus.pc.framework;

public class HttpReps {
    Boolean success;
    Status status;
    String message;
    Object data;

    public HttpReps(Boolean success, Status status, String message, Object data) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static HttpReps ok(String message) {
        return new HttpReps(true, Status.Ok, message, null);
    }
    public static HttpReps ok(String message, Object data) {
        return new HttpReps(true, Status.Ok, message, data);
    }
    public static HttpReps failed(String message) {
        return new HttpReps(false, Status.Failed, message, null);
    }
    public static HttpReps failed(String message, Object data) {
        return new HttpReps(false, Status.Failed, message, data);
    }
    public static HttpReps error(String message) {
        return new HttpReps(false, Status.Error, message, null);
    }
    public static HttpReps error(String message, Object data) {
        return new HttpReps(false, Status.Error, message, data);
    }
    public static HttpReps invalidArgument(String message) {
        return new HttpReps(false, Status.InvalidArgument, message, null);
    }
    public static HttpReps invalidArgument(String message, Object data) {
        return new HttpReps(false, Status.InvalidArgument, message, data);
    }
    public static HttpReps permissionDenied(String message) {
        return new HttpReps(false, Status.PermissionDenied, message, null);
    }
    public static HttpReps permissionDenied(String message, Object data) {
        return new HttpReps(false, Status.PermissionDenied, message, data);
    }
    public static HttpReps exceptionOccurred(String message) {
        return new HttpReps(false, Status.ExceptionOccurred, message, null);
    }
    public static HttpReps exceptionOccurred(String message, Object data) {
        return new HttpReps(false, Status.ExceptionOccurred, message, data);
    }
    public static HttpReps other(String message) {
        return new HttpReps(false, Status.Other, message, null);
    }
    public static HttpReps other(String message, Object data) {
        return new HttpReps(false, Status.Other, message, data);
    }

    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    public static enum Status {
        Ok,                 // 成功
        Failed,             // 失败
        Error,              // 错误
        InvalidArgument,    // 参数无效
        PermissionDenied,   // 未授权
        ExceptionOccurred,  // 发生异常
        Other               // 其它
    }
}
