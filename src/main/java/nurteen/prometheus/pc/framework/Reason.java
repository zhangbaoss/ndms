package nurteen.prometheus.pc.framework;

public class Reason {
    Status status;
    String message;

    public Reason() {
    }
    public Reason(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public static Reason ok(String message) {
        return new Reason(Status.Ok, message);
    }
    public static Reason failed(String message) {
        return new Reason(Status.Failed, message);
    }
    public static Reason error(String message) {
        return new Reason(Status.Error, message);
    }
    public static Reason invalidArgument(String message) {
        return new Reason(Status.InvalidArgument, message);
    }
    public static Reason permissionDenied(String message) {
        return new Reason(Status.PermissionDenied, message);
    }
    public static Reason exceptionOccurred(String message) {
        return new Reason(Status.ExceptionOccurred, message);
    }
    public static Reason other(String message) {
        return new Reason(Status.Other, message);
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
