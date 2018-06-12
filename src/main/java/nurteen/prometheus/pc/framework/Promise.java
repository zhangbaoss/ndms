package nurteen.prometheus.pc.framework;

public class Promise<T> {
    Status status;
    Resolved<T> resolved;
    Rejected rejected;
    T value;
    String reason;

    public Promise(Executor<T> executor) {
        this.status = Status.Pending;
        executor.execute(
            (value) -> {
                this.status = Status.Resolved;
                if (this.resolved != null) {
                    this.resolved.resolve(value);
                }
            },
            (reason) -> {
                this.status = Status.Rejected;
                if (this.rejected != null) {
                    this.rejected.reject(reason);
                }
            }
        );
    }

    public void resolve(Resolved<T> resolved) {
        if (status == Status.Pending) {
            this.resolved = resolved;
        }
        else if (status == Status.Resolved) {
            resolved.resolve(value);
        }
    }
    public void reject(Rejected rejected) {
        if (status == Status.Pending) {
            this.rejected = rejected;
        }
        else if (status == Status.Rejected) {
            rejected.reject(reason);
        }
    }

    static enum Status {
        Pending,        // 进行中
        Resolved,       // 已经成功
        Rejected        // 已经失败
    }
    public static interface Resolved<T> {
        void resolve(T value);
    }
    public static interface Rejected {
        void reject(String reason);
    }
    public static interface Executor<T> {
        void execute(Resolved<T> resolve, Rejected reject);
    }
}
