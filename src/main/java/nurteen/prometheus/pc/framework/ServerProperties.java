package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.entities.DeviceInfo;
import nurteen.prometheus.pc.framework.entities.DeviceType;

public class ServerProperties {
    static String ndid;

    public final static String getNdid() {
        if (ndid != null) {
            return ndid;
        }
        
        synchronized (ServerProperties.class) {
        	if (ndid != null) {
	            try {
	            	DeviceInfo deviceInfo = ObjectFactory.storageAware.fromHid("");
	            	if (deviceInfo != null) {
	            		ndid = deviceInfo.getNdid();
	            	}
	            	else {
	            		// ndid = ObjectFactory.storageAware.genNdid(DeviceType.Center_Pc);
	            		deviceInfo = new DeviceInfo(DeviceType.Center_Pc, 1, "");
	            		ObjectFactory.storageAware.insertNew(deviceInfo);
	            		ndid = deviceInfo.getNdid();
	            	}
	                
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }
        	}
        }
        return ndid;
    }
}
