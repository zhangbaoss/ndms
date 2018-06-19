package nurteen.prometheus.pc.framework.utils;

import java.io.IOException;

import org.nustaq.serialization.FSTConfiguration;

import nurteen.prometheus.pc.framework.session.SessionSharingSession;

/**
 * fst序列化工具
 * https://github.com/RuedigerMoeller/fast-serialization
 *
 */
public class SerializeUtil{
	
	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
	/**
	 * 请将常用的序列化/反序列化 对象注册在这里，可以加快速度
	 */
	static {
		conf.registerClass(SessionSharingSession.class,String.class);
		conf.setForceSerializable(true);
	}
	/**
	 * 对象转换成byte[]
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] objToBytes(Object obj){
		return conf.asByteArray(obj);
	}
	
	/**
	 * byte[]转换成对象
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object bytesToObj(byte[] bytes) {
		return conf.asObject(bytes);
	}

}
