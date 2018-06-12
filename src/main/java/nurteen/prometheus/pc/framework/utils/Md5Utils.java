package nurteen.prometheus.pc.framework.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Md5Utils {
    public static String md5(String value) {
        return md5(value.getBytes(StandardCharsets.UTF_8));
    }
    public static String md5(byte[] value) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] summary = m.digest(value);
            StringBuilder builder = new StringBuilder();
            for (byte ch: summary) {
                builder.append(Integer.toHexString(ch & 0xff));
            }
            return builder.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
