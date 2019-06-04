package game.world;

import static util.SerializeUtil.*;
import game.entity.Player;
import java.util.BitSet;
import game.ui.Action;
import game.GameSetting;
import java.util.concurrent.ConcurrentLinkedQueue;
import static util.MathUtil.*;

public class PlayerInfo implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	public volatile Player player;
	transient float H_div_W=0;
	//public transient volatile GameSetting game_setting;
	transient volatile byte[] ni=null;
	transient volatile long last_remove_time=0;
	transient int skip_t=0;
	transient BitSet known_id=null;
	transient ConcurrentLinkedQueue<Action> action_queue=null;
	public void add(){
		skip_t=0;
		player.resetUI();
		ni=null;
		action_queue=new ConcurrentLinkedQueue<>();
		known_id=new BitSet(32768);
		
		if(!player.removed)return;
		player.removed=false;
		if(last_remove_time!=World.cur.time)player.add();
	}
	public void remove(){
		known_id=null;
		ni=null;
		player.removed=true;
		last_remove_time=World.cur.time;
		action_queue=null;
	}
	void readAction(){
		if(player.removed)return;
		if(!action_queue.isEmpty()){
			try{
				while(!action_queue.isEmpty()){
					Action a=action_queue.poll();
					H_div_W=a.height/(a.width+1e-3f);
					for(short x:a.known_id)known_id.set(x);
					player.action.upd(a);
				}
			}catch(Exception e){e.printStackTrace();}
			skip_t=0;
		}else ++skip_t;
	}
	void genNI(){
		if(player.removed||ni!=null)return;
		ni=World.cur.getNearby(player).getBytes((BitSet)known_id.clone(),H_div_W);
	}
	public void setAction(byte[]a)throws Exception{
		/*int len=Math.min(a.length,16);
		char s[]=new char[len*2],ss[]="0123456789ABCDEF".toCharArray();
		for(int i=0;i<len;++i){
			s[i*2]=ss[a[i]&15];
			s[i*2+1]=ss[a[i]>>4&15];
		}
		debug.Log.i(new String(s)+":"+a.length);*/
		
		
		if(player.removed)throw new Exception("Player has been removed");
		try{
			action_queue.offer((Action)bytes2obj(a));
		}catch(Exception e){
			debug.Log.i(e);
		}
	}
	public byte[] getNI(){
		byte[] w=ni;
		ni=null;
		return w;
	}
	boolean check(String un,String pw){return false;}
	boolean check0(String un){return false;}
	public String toString(){
		if(player.removed)return "[removed]";
		return getUserName()+"  "+player.name+"  "+f2i(player.x)+","+f2i(player.y)+"  "+(player.creative_mode?"创造模式":"生存模式");
	}
	public String getUserName(){
		return "[root]";
	}
}
class RemotePlayerInfo extends PlayerInfo{
	private static final long serialVersionUID=1844677L;
	String username;
	String password;
	RemotePlayerInfo(String un,String pw){
		username=un;
		password=pw;
	}
	boolean check(String un,String pw){
		return player.removed&&un.equals(username)&&pw.equals(password);
	}
	boolean check0(String un){return un.equals(username);}
	public String getUserName(){
		return username;
	}
}
