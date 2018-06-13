package nurteen.prometheus.pc.framework;

public interface BasePromise<T> {
    void resolve(T value);
    void reject(String reason);
}
