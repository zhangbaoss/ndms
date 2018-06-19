package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.entities.DeviceType;
import nurteen.prometheus.pc.framework.entities.ThirdpartyAccountType;
import nurteen.prometheus.pc.framework.entities.DeviceInfo;
import nurteen.prometheus.pc.framework.entities.UserInfo;
import nurteen.prometheus.pc.framework.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

public abstract class StorageAware {

    private static String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Autowired
    protected ServerConfigProperties configProperties;

    // nuid为9位
    // 2位配置（不同服务器不一样）+取本地时间毫秒值（转换位62进制后位7位）
    public final String genNuid() throws Exception {
        return this.genNuid(this.configProperties.getIdGenPrefix());
    }
    public String genNuid(String prefix) throws Exception {
        /*
        Instant instant = this.getInstant();
        long msecs = instant.getEpochSecond() * 1000L;
        msecs += instant.getNano() / 1000 / 1000;
        */
        long msecs = TimeUtils.currentTimeMsecs();
        StringBuilder s = new StringBuilder(prefix);
        this.transfrom(s, msecs, 62);
        return s.toString();
    }

    // naid_aas9ca8wxel0mb
    public final String genAccount() throws Exception {
        return this.genAccount(this.configProperties.getIdGenPrefix());
    }
    public String genAccount(String prefix) throws Exception {
        /*
        Instant instant = this.getInstant();
        long nsecs = instant.getEpochSecond() * 1000L * 1000L * 1000L;
        nsecs += instant.getNano();
        */
        long nsecs = TimeUtils.currentTimeNsecs();
        StringBuilder s = new StringBuilder("naid_").append(prefix.toLowerCase());
        this.transfrom(s, nsecs, 36);
        return s.toString();
    }

    // ndid为12位
    // 1位（设备类型）+2位配置（不同服务器不一样）+取本地时间微秒值（转换位62进制后位9位）
    public final String genNdid(DeviceType type) throws Exception {
        return this.genNdid(this.configProperties.getIdGenPrefix(), type);
    }
    public String genNdid(String prefix, DeviceType type) throws Exception {
        /*
        Instant instant = this.getInstant();
        long usecs = instant.getEpochSecond() * 1000L * 1000L;
        usecs += instant.getNano() / 1000;
        */
        long usecs = TimeUtils.currentTimeUsecs();
        StringBuilder s = new StringBuilder().append(chars.charAt(type.getValue())).append(prefix);
        this.transfrom(s, usecs, 62);
        return s.toString();
    }




    public abstract Instant getInstant() throws Exception;

    public abstract UserInfo fromNuid(String nuid) throws Exception;
    public abstract UserInfo fromAccount(String account, String password) throws Exception;
    public abstract UserInfo fromThirdpartyAccount(ThirdpartyAccountType type, String account) throws Exception;
    public abstract UserInfo fromPhone(String phone, String password) throws Exception;
    public abstract void insertNew(UserInfo userInfo) throws Exception;
    public abstract void insertNewFromThirdparty(UserInfo userInfo, ThirdpartyAccountType type, String account) throws Exception;

    public abstract DeviceInfo fromNdId(String ndid) throws Exception;
    public abstract DeviceInfo fromHid(String hid) throws Exception;
    public abstract String getDeviceName(String nuid, String ndid) throws Exception;
    public abstract void insertNew(String nuid, String ndid, String name) throws Exception;
    public abstract void insertNew(DeviceInfo deviceInfo) throws Exception;
    public abstract void insertNew(String nuid, String name, DeviceInfo deviceInfo) throws Exception;

    // 进制转换，值被反转
    private void transfrom(StringBuilder s, long val, int radix) {
        do {
            s.append(chars.charAt((int) (val % radix)));
            val /= radix;
        } while (val > 0);
    }
}
