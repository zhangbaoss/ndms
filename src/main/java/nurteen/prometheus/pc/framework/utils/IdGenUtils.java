package nurteen.prometheus.pc.framework.utils;

import nurteen.prometheus.pc.framework.ObjectFactory;
import nurteen.prometheus.pc.framework.entities.DeviceType;

public class IdGenUtils {
    // nuid
    public static final String genNuid() throws Exception {
        return ObjectFactory.storageAware.genNuid();
    }

    // account
    public static final String genAccount() throws Exception {
        return ObjectFactory.storageAware.genAccount();
    }

    // ndid
    public static final String genNdid(DeviceType type) throws Exception {
        return ObjectFactory.storageAware.genNdid(type);
    }

    // hid
    public static String genHid() {
        return "";
    }
}
