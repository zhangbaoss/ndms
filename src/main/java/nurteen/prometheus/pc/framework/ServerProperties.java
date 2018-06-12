package nurteen.prometheus.pc.framework;

public class ServerProperties {
    static String ndid;

    public final static String getNdid() {
        if (ndid != null) {
            return ndid;
        }
        synchronized (ServerProperties.class) {
            try {
                ndid = ObjectFactory.storageAware.genNdid(Constants.DEVICE_TYPE_PC_CENTER);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ndid;
    }
}
