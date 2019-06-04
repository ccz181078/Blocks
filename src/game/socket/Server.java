package game.socket;

import java.io.*;
import java.net.*;
import com.ccz.blocks.MainActivity;
import static com.ccz.blocks.MainActivity.showText;
import util.AddressGetter;
import game.GlobalSetting;

public class Server implements Runnable{
	private static final long serialVersionUID=1844677L;
	private static int time_out=5000;
	static ServerSocket ss;
	
	private long rnd_id;
	public void start(){
		if(!GlobalSetting.getInstance().server_on)return;
		rnd_id=MainActivity.rnd_id;
		new Thread(this).start();
	}
	public void run(){
		if(ss!=null){
			try{ss.close();}catch(Exception e){}
			ss=null;
		}
		String ip=GlobalSetting.getInstance().default_server_ip;
		try{
			if(ip.equals(""))throw new Exception();
			ss=new ServerSocket(18447,16,util.AddressGetter.str2ip(ip));
		}catch(Exception ex){
			if(!ip.equals(""))showText(ip+" 不可用");
			try{
				ss=new ServerSocket(18447,16,util.AddressGetter.getIp());
			}catch(Exception e){
				e.printStackTrace();
				showText("服务器启动失败\n"+e.toString());
				return;
			}
		}
		showText("服务器已启动\nip:\n"+ss.getInetAddress().getHostAddress());
		try{
			ss.setSoTimeout(10);
			while(MainActivity.rnd_id==rnd_id){
				Socket s=null;
				while(s==null){
					try{s=Server.ss.accept();}catch(SocketTimeoutException e){}
					try{Thread.sleep(30);}catch(Exception e){}
				}
				new ServerThread(rnd_id,s);
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
