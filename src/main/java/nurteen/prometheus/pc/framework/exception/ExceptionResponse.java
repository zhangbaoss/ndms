package nurteen.prometheus.pc.framework.exception;

import java.util.ArrayList;
import java.util.List;

public class ExceptionResponse {
    List<String> stackTraces;

    public ExceptionResponse(StackTraceElement[] stackTraceElements) {
        this.stackTraces = new ArrayList<>();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            this.stackTraces.add(String.format("at %s.%s(%s:%d)", stackTraceElement.getClassName(),
                stackTraceElement.getMethodName(), stackTraceElement.getFileName(), stackTraceElement.getLineNumber()));
        }
    }

    public List<String> getStackTraces() {
        return stackTraces;
    }
    public void setStackTraces(List<String> stackTraces) {
        this.stackTraces = stackTraces;
    }
}
