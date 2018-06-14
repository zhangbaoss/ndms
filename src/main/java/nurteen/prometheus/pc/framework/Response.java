package nurteen.prometheus.pc.framework;

public class Response {
    Boolean success;
    Status status;
    String reason;

    public Response() {
    }
    public Response(Boolean success, Status status, String reason) {
        this.success = success;
        this.status = status;
        this.reason = reason;
    }

    public static Response ok(String message) {
        return new Response(true, Status.Ok, message);
    }
    public static Response ok(String message, Object data) {
        return new DataResponse(true, Status.Ok, message, data);
    }
    public static Response failed(String message) {
        return new Response(false, Status.Failed, message);
    }
    public static Response failed(String message, Object data) {
        return new DataResponse(false, Status.Failed, message, data);
    }
    public static Response error(String message) {
        return new Response(false, Status.Error, message);
    }
    public static Response error(String message, Object data) {
        return new DataResponse(false, Status.Error, message, data);
    }
    public static Response invalidArgument(String message) {
        return new Response(false, Status.InvalidArgument, message);
    }
    public static Response invalidArgument(String message, Object data) {
        return new DataResponse(false, Status.InvalidArgument, message, data);
    }
    public static Response permissionDenied() {
        return new Response(false, Status.PermissionDenied, "未授权，请先进行登录认证，然后再重试！");
    }
    public static Response permissionDenied(String message) {
        return new Response(false, Status.PermissionDenied, message);
    }
    public static Response permissionDenied(String message, Object data) {
        return new DataResponse(false, Status.PermissionDenied, message, data);
    }
    public static Response exceptionOccurred(String message) {
        return new Response(false, Status.ExceptionOccurred, message);
    }
    public static Response exceptionOccurred(String message, Object data) {
        return new DataResponse(false, Status.ExceptionOccurred, message, data);
    }
    public static Response other(String message) {
        return new Response(false, Status.Other, message);
    }
    public static Response other(String message, Object data) {
        return new DataResponse(false, Status.Other, message, data);
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

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
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

    public static class DataResponse extends Response {
        Object data;

        public DataResponse(Boolean success, Status status, String reason, Object data) {
            super(success, status, reason);
            this.data = data;
        }

        public Object getData() {
            return data;
        }
        public void setData(Object data) {
            this.data = data;
        }
    }
}
