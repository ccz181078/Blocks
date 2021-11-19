package game.socket;

import java.io.*;
import java.net.*;
import game.world.PlayerInfo;
import game.world.World;
import game.GameSetting;
import static game.world.World.showText;
import static util.SerializeUtil.*;
import util.*;

public class ServerThread implements Runnable{
	private static int time_out=15000;
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
		boolean logined=false;
		try{
			s.setSoTimeout(time_out);
			final DataInputStream is=new DataInputStream(s.getInputStream());
			final DataOutputStream os=new DataOutputStream(s.getOutputStream());
			un=is.readUTF();
			pw=is.readUTF();
			final GameSetting gs=(GameSetting)bytes2obj(readBytes(is));
			debug.Log.i(un+" 已连接:\n"+s.getInetAddress().getHostAddress()+":"+s.getPort()+"\n"+un+"\n"+pw);
			if(gs.player_name.equals("player")||gs.player_name.equals(""))gs.player_name=un;
			World.cur.runOnUpdateThread(new Runnable(){public void run(){
				try{
					state.obj=new Boolean(World.cur.login(un,pw,pi,gs));
				}catch(Exception e){debug.Log.i(e);}
			}});
			long tt=System.currentTimeMillis();
			while(state.obj==null){
				Thread.sleep(3);
				if(System.currentTimeMillis()-tt>time_out)throw new Exception("Timeout");
			}
			if(!state.obj)throw new Exception(un+" login failed");
			showText(un+" 已登录");
			
			logined=true;

			final Pointer<Exception> pex=new Pointer<>();
			final CompressedInputStream cis=new CompressedInputStream(s.getInputStream());
			final CompressedOutputStream cos=new CompressedOutputStream(s.getOutputStream());
			new Thread(){
				public void run(){
					try{
						while(pex.obj==null){
							//long tt=System.currentTimeMillis();
							pi.obj.setAction(cis.read());
							Thread.sleep(3);
							//sleep(Math.max(5,33-(System.currentTimeMillis()-tt)));
						}
					}catch(Exception e){
						pex.obj=e;
					}
				}
			}.start();
			o1:
			while(World.cur.rnd_id==rnd_id&&pi.obj.player.online){
				byte[] b=null;
				tt=System.currentTimeMillis();
				while((b=pi.obj.getNI())==null){
					if(!pi.obj.player.online)break o1;
					if(World.cur.rnd_id!=rnd_id)break o1;
					Thread.sleep(3);
					if(System.currentTimeMillis()-tt>time_out)throw new Exception("Timeout");
					if(pex.obj!=null)throw pex.obj;
				}
				cos.write(b);
				Thread.sleep(3);
			}
			throw new Exception();
		}catch(Exception e){
			showText(un+" 已断开连接");
			debug.Log.i(e);
			if(logined)
			try{
				World.cur.runOnUpdateThread(new Runnable(){public void run(){
					try{
						World.cur.logout(un);
					}catch(Exception e){debug.Log.i(e);}
				}});
			}catch(Exception ex){ex.printStackTrace();}
			try{s.close();}catch(Exception ex){e.printStackTrace();}
		}
	}
}
