package game.socket;

import java.io.*;
import java.net.*;
import static game.world.World.showText;
import util.AddressGetter;
import game.GlobalSetting;
import game.world.World;

public class Server implements Runnable{
	private static final long serialVersionUID=1844677L;
	private static int time_out=15000;
	static ServerSocket ss;
	
	private long rnd_id;
	public void start(){
		if(!GlobalSetting.getInstance().server_on)return;
		rnd_id=World.cur.rnd_id;
		new Thread(this).start();
	}
	public static void close(){
		if(ss!=null){
			try{ss.close();}catch(Exception e){}
			ss=null;
		}
	}
	public void run(){
		close();
		String ip=GlobalSetting.getInstance().default_server_ip;
		/*try{
			if(ip.equals(""))throw new Exception();
			ss=new ServerSocket(18447,16,util.AddressGetter.str2ip(ip));
		}catch(Exception ex){
			if(!ip.equals(""))showText(ip+" 不可用");*/
		try{
			ss=new ServerSocket(18447,16,InetAddress.getByAddress(new byte[4]));
		}catch(Exception e){
			e.printStackTrace();
			showText("服务器启动失败\n"+e.toString());
			return;
		}
		//}
		showText("服务器已启动\nip:\n"+util.AddressGetter.getIp());
		try{
			ss.setSoTimeout(10);
			while(World.cur.rnd_id==rnd_id){
				Socket s=null;
				while(s==null&&World.cur.rnd_id==rnd_id){
					try{s=Server.ss.accept();}catch(SocketTimeoutException e){}
					Thread.yield();
				}
				new ServerThread(rnd_id,s);
			}
		}catch(Exception e){}
		System.out.println("Server exited");
	}
}
