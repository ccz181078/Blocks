package game.socket;

import java.net.*;
import java.io.*;
import static util.SerializeUtil.*;
import game.world.NearbyInfo;
import com.ccz.blocks.*;
import util.Pointer;
import static com.ccz.blocks.MainActivity.showText;

public class Client implements Runnable{
	private static final long serialVersionUID=1844677L;
	private static int time_out=5000;
	private InetAddress iaddr;
	private String addr,user_name,password;
	private long rnd_id=0;
	public static Client cur;
	public Client(String ip0,String un,String pw)throws Exception{
		//iaddr=util.AddressGetter.str2ip(ip0);
		addr=ip0;
		user_name=un;
		password=pw;
	}
	public void run(){
		Socket s=null;
		try{
			iaddr=util.AddressGetter.str2ip(addr);
			s=new Socket();
			s.connect(new InetSocketAddress(iaddr,18447),time_out);
			showText("已连接");
			s.setSoTimeout(time_out);
			DataInputStream is=new DataInputStream(s.getInputStream());
			DataOutputStream os=new DataOutputStream(s.getOutputStream());
			os.writeUTF(user_name);
			os.writeUTF(password);
			writeBytes(os,obj2bytes(game.GlobalSetting.getGameSetting()));
			os.flush();
			
			for(;;){
				GameView.ni=readBytes(is);
				//android.util.Log.i("ni",GameView.ni.length+" bytes");
				final Pointer<byte[]> action=new Pointer<>();
				if(MainActivity.rnd_id!=rnd_id)break;
				MainActivity._this.runOnUiThread(new Runnable(){public void run(){
					action._=MainActivity._this.action.getBytes();
					//android.util.Log.i("action",action._.length+" bytes");
					MainActivity._this.game_view.invalidate();
				}});
				long tt=System.currentTimeMillis();
				while(action._==null){
					try{Thread.sleep(3);}catch(Exception e){e.printStackTrace();}
					if(System.currentTimeMillis()-tt>time_out)throw new Exception("TimeOut");
				}
				writeBytes(os,action._);
				os.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			showText("已断开连接");
		}finally{
			try{s.close();}catch(Exception e){}
			if(MainActivity.rnd_id==rnd_id)try{
				final MainActivity ctx=MainActivity._this;
				ctx.runOnUiThread(new Runnable(){public void run(){
					ctx.finish();
				}});
			}catch(Exception e){}
		}
	}
	public void connect(){
		if(rnd_id!=0)MainActivity._this.finish();
		rnd_id=MainActivity.rnd_id;
		new Thread(this).start();
	}
}
