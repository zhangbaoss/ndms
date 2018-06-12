package nurteen.prometheus.pc.framework.utils;

public final class TimeUtils {
    public static long secTime() {
        return System.nanoTime() / 1000L / 1000L / 1000L;
    }
    public static long msecTime() {
        return System.nanoTime() / 1000L / 1000L;
    }
    public static long usecTime() {
        return System.nanoTime() / 1000L;
    }
    public static long nsecTime() {
        return System.nanoTime();
    }
}
