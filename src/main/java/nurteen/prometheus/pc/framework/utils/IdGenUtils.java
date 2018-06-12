package nurteen.prometheus.pc.framework.utils;

public class IdGenUtils {
    static String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // nuid为9位
    // 2位配置（不同服务器不一样）+取本地时间毫秒值（转换位62进制后位7位）
    public static String genNuid() {
        return "AB" + to62(System.currentTimeMillis());
    }

    // ndid为12位
    // 2位配置（不同服务器不一样）+1位（设备类型）+取本地时间微秒值（转换位62进制后位9位）
    public static String genNdid() {
        return "";
    }

    private static String to62(long value) {
        StringBuilder s = new StringBuilder();
        while (value > 0) {
            s.append(chars.charAt((int)(value % 62)));
            value /= 62;
        }
        return s.toString();
    }
}
