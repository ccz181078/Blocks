package util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.apache.http.conn.util.InetAddressUtils;

public class AddressGetter{
	private static final long serialVersionUID=1844677L;
	public static InetAddress getIp(){
		try{
			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			en.hasMoreElements();){
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
				.hasMoreElements();){
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(inetAddress
									.getHostAddress())){
						if (inetAddress.getHostAddress().startsWith("10.0.2."))
							continue;
						else
							return inetAddress;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return getLocalIpAddress();
	}

	private static InetAddress getLocalIpAddress() {
		InetAddress ipaddress = null;
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ipaddress = inetAddress;
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return ipaddress;
	}
	
	public static InetAddress str2ip(String str)throws Exception{
		return InetAddress.getByName(str);
		/*String[] ss=str.split("\\.");
		if(ss.length!=4)throw new Exception();
		byte[] ip=new byte[4];
		for(int i=0;i<4;++i){
			int x=Integer.valueOf(ss[i]);
			if((x&~255)!=0)throw new Exception();
			ip[i]=(byte)x;
		}
		return InetAddress.getByAddress(ip);*/
	}
}
