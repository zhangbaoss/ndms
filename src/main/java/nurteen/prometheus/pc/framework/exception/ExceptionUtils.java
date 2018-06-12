package nurteen.prometheus.pc.framework.exception;

public class ExceptionUtils {

    public static void printAndThrow(Exception e) throws Exception {
        e.printStackTrace();
        throw e;
    }
}
