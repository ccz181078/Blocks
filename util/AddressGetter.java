package util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
//import org.apache.http.conn.util.InetAddressUtils;

public class AddressGetter{
	private static final long serialVersionUID=1844677L;
	public static InetAddress getIp(){
		/*try{
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
		}*/
		return getLocalHostLANAddress();
		//return getLocalIpAddress();
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
	public static InetAddress getLocalHostLANAddress(){
		try {
			InetAddress candidateAddress = null;
			// 遍历所有的网络接口
			for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// 在所有的接口下再遍历IP
				for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
						if (inetAddr.isSiteLocalAddress()) {
							// 如果是site-local地址，就是它了
							return inetAddr;
						} else if (candidateAddress == null) {
							// site-local类型的地址未被发现，先记录候选地址
							candidateAddress = inetAddr;
						}
					}
				}
			}
			if (candidateAddress != null) {
				return candidateAddress;
			}
			// 如果没有发现 non-loopback地址.只能用最次选的方案
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			return jdkSuppliedAddress;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void showIpAddress() {
		System.out.println("----------------");
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					System.out.println(inetAddress.toString());
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		System.out.println("----------------");
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
