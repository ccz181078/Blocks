package game.socket;

import java.net.*;
import java.io.*;
import static util.SerializeUtil.*;
import game.world.NearbyInfo;
import com.ccz.blocks.*;
import util.*;
import static com.ccz.blocks.MainActivity.showText;

public class Client implements Runnable{
	private static final long serialVersionUID=1844677L;
	private static int time_out=15000;
	private InetAddress iaddr;
	private String addr,user_name,password;
	private long rnd_id=0;
	private int port=18447;
	public static Client cur;
	public Client(String ip0,int port0,String un,String pw)throws Exception{
		//iaddr=util.AddressGetter.str2ip(ip0);
		addr=ip0;
		port=port0;
		user_name=un;
		password=pw;
	}
	public void run(){
		for(;;){
		Socket s=null;
		final Pointer<Exception> pex=new Pointer<>();
		final int[] conn_time=new int[]{0};
		try{
			//iaddr=util.AddressGetter.str2ip(addr);
			s=new Socket();
			s.connect(new InetSocketAddress(addr,port),time_out);
			showText("已连接");
			s.setSoTimeout(time_out);
			final DataInputStream is=new DataInputStream(s.getInputStream());
			final DataOutputStream os=new DataOutputStream(s.getOutputStream());
			os.writeUTF(user_name);
			os.writeUTF(password);
			writeBytes(os,obj2bytes(game.GlobalSetting.getGameSetting()));
			os.flush();
			

			final CompressedInputStream cis=new CompressedInputStream(s.getInputStream());
			final CompressedOutputStream cos=new CompressedOutputStream(s.getOutputStream());
			new Thread(){
				public void run(){
					try{
						while(pex.obj==null){
							final Pointer<byte[]> action=new Pointer<>();
							MainActivity._this.runOnUiThread(new Runnable(){public void run(){
								try{
									action.obj=MainActivity._this.action.getBytes();
								}catch(Exception e){pex.obj=e;}
							}});
							long tt=System.currentTimeMillis();
							while(action.obj==null){
								Thread.sleep(3);
								if(System.currentTimeMillis()-tt>time_out)throw new Exception("TimeOut");
							}
							cos.write(action.obj);
							sleep(Math.max(5,33-(System.currentTimeMillis()-tt)));
						}
					}catch(Exception e){
						pex.obj=e;
					}
				}
			}.start();
			while(MainActivity.rnd_id==rnd_id){
				conn_time[0]+=1;
				GameView.ni.offer(cis.read());
				MainActivity._this.runOnUiThread(new Runnable(){public void run(){
					try{
						MainActivity._this.game_view.invalidate();
					}catch(Exception e){pex.obj=e;}
				}});
				if(pex.obj!=null)throw pex.obj;
				Thread.sleep(3);
			}
			throw new Exception();
		}catch(Exception e){
			if(e instanceof EOFException)showText("服务器切断了连接");
			pex.obj=e;
			debug.Log.i(e);
			showText("已断开连接");
			try{s.close();}catch(Exception ex){}
			if(MainActivity._this!=null&&MainActivity.rnd_id==rnd_id){
				/*if(conn_time[0]<=300||true){
					try{
						final MainActivity ctx=MainActivity._this;
						ctx.runOnUiThread(new Runnable(){public void run(){ctx.finish();}});
					}catch(Exception ex){}
					return;
				}*/
				try{
					Thread.sleep(3000);
				}catch(Exception ex){}
				try{
					final MainActivity ctx=MainActivity._this;
					ctx.runOnUiThread(new Runnable(){public void run(){ctx.action.init();}});
				}catch(Exception ex){}
			}else return;
		}
		}
	}
	public void connect(){
		if(rnd_id!=0){
			MainActivity._this.finish();
			return;
		}
		rnd_id=MainActivity.rnd_id;
		new Thread(this).start();
	}
}
