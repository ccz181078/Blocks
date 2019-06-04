package game.socket;

import java.io.*;
import java.net.*;
import game.world.PlayerInfo;
import game.world.World;
import util.Pointer;
import com.ccz.blocks.MainActivity;
import game.GameSetting;
import static com.ccz.blocks.MainActivity.showText;
import static util.SerializeUtil.*;

public class ServerThread implements Runnable{
	private static int time_out=5000;
	long rnd_id;
	Socket s;
	Pointer<PlayerInfo> pi=new Pointer<>();
	Pointer<Boolean> state=new Pointer<>();
	String un,pw;
	ServerThread(long _rnd_id,Socket _s)throws Exception{
		rnd_id=_rnd_id;
		s=_s;
		new Thread(this).start();
	}
	public void run(){
		un="";
		try{
			s.setSoTimeout(time_out);
			DataInputStream is=new DataInputStream(s.getInputStream());
			DataOutputStream os=new DataOutputStream(s.getOutputStream());
			un=is.readUTF();
			pw=is.readUTF();
			GameSetting gs=(GameSetting)bytes2obj(readBytes(is));
			showText(un+" 已连接:\n"+s.getInetAddress().getHostAddress()+":"+s.getPort()+"\n"+un+"\n"+pw);
			MainActivity._this.runOnUiThread(new Runnable(){public void run(){
				try{
					state._=new Boolean(World.login(un,pw,pi));
				}catch(Exception e){debug.Log.i(e);}
			}});
			long tt=System.currentTimeMillis();
			while(state._==null){
				try{Thread.sleep(10);}catch(Exception e){e.printStackTrace();}
				if(System.currentTimeMillis()-tt>time_out)throw new Exception("Timeout");
			}
			if(!state._)throw new Exception(un+" login failed");
			showText(un+" 已登录");
			pi._.game_setting=gs;
			while(MainActivity.rnd_id==rnd_id){
				byte[] b=null;
				tt=System.currentTimeMillis();
				while((b=pi._.getNI())==null){
					try{Thread.sleep(7);}catch(Exception e){e.printStackTrace();}
					if(System.currentTimeMillis()-tt>time_out)throw new Exception("Timeout");
				}
				writeBytes(os,b);
				os.flush();
				pi._.setAction(readBytes(is));
			}
		}catch(Exception e){
			showText(un+" 已断开连接");
			debug.Log.i(e);
		}finally{
			try{
				MainActivity._this.runOnUiThread(new Runnable(){public void run(){
					try{
						pi._.remove();
					}catch(Exception e){debug.Log.i(e);}
				}});
			}catch(Exception e){e.printStackTrace();}
			try{s.close();}catch(Exception e){e.printStackTrace();}
		}
	}
}
