package nurteen.prometheus.pc.framework.utils;

public final class TimeUtils {
    public static long currentTimeSecs() {
        return currentTimeNsecs() / 1000L / 1000L / 1000L;
    }
    public static long currentTimeMsecs() {
        return currentTimeNsecs() / 1000L / 1000L;
    }
    public static long currentTimeUsecs() {
        return currentTimeNsecs() / 1000L;
    }
    public static native long currentTimeNsecs();

    public static long secsTime() {
        return System.nanoTime() / 1000L / 1000L / 1000L;
    }
    public static long msecsTime() {
        return System.nanoTime() / 1000L / 1000L;
    }
    public static long usecsTime() {
        return System.nanoTime() / 1000L;
    }
    public static long nsecsTime() {
        return System.nanoTime();
    }

    static {
        System.loadLibrary("time-utils");
    }
}
