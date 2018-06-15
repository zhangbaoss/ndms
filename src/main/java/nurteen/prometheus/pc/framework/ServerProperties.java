package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.entities.DeviceInfo;
import nurteen.prometheus.pc.framework.entities.DevicePlatform;
import nurteen.prometheus.pc.framework.entities.DeviceType;
import nurteen.prometheus.pc.framework.utils.ContainerUtils;

import java.util.List;

public class ServerProperties {
    static String ndid;

    public final static String getNdid() {
        if (ndid != null) {
            return ndid;
        }
        
        synchronized (ServerProperties.class) {
        	if (ndid == null) {
	            try {
	            	DeviceInfo deviceInfo = ObjectFactory.storageAware.fromHid("");
	            	if (deviceInfo == null) {
	            		deviceInfo = new DeviceInfo(DeviceType.Center_Pc, DevicePlatform.Linux, "");
	            		ObjectFactory.storageAware.insertNew(deviceInfo);
	            	}

					ndid = deviceInfo.getNdid();
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }
        	}
        }
        return ndid;
    }
	public final static List<String> getNdidList() {
    	return ContainerUtils.makeArrayList(getNdid()).get();
	}
}
