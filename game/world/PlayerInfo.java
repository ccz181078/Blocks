package game.world;

import static util.SerializeUtil.*;
import game.entity.Player;
import java.util.BitSet;
import game.ui.Action;
import game.GameSetting;
import java.util.LinkedList;
import static util.MathUtil.*;

public class PlayerInfo implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	public final Player player;
	transient float H_div_W=0;
	//public transient volatile GameSetting game_setting;
	transient volatile byte[] ni=null;
	//transient volatile long last_remove_time=0;
	transient int skip_t=0;
	transient BitSet known_id=null;
	transient LinkedList<Action> action_queue=null;
	transient util.ScreenSaver ss=null;
	PlayerInfo(Player pl){
		player=pl;
	}
	public void add(){
		skip_t=0;
		player.resetUI();
		ni=null;
		action_queue=new LinkedList<>();
		known_id=new BitSet(32768);
		player.online=true;
		
		/*if(!player.removed)return;
		player.removed=false;
		if(last_remove_time!=World.cur.time)player.add();*/
	}
	public void remove(){
		known_id=null;
		ni=null;
		player.online=false;
		//player.removed=true;
		//last_remove_time=World.cur.time;
		action_queue=null;
	}
	synchronized void applyAction(LinkedList<Action> action_queue){
		if(action_queue==null)return;
		if(!action_queue.isEmpty()){
			try{
				for(Action a:action_queue){
					H_div_W=a.height/(a.width+0f);
					for(short x:a.known_id)known_id.set(x);
					player.action.upd(a);
				}
			}catch(Exception e){e.printStackTrace();}
			skip_t=0;
		}else ++skip_t;
	}
	synchronized LinkedList<Action> readAction(){
		if(!player.online)return null;
		LinkedList<Action> ret=action_queue;
		action_queue=new LinkedList<>();
		return ret;
	}
	void genNI(){
		if(!player.online||ni!=null)return;
		ni=World.cur.getNearby(player).getBytes((BitSet)known_id.clone(),H_div_W);
		if(ss!=null)ss.write(ni);
	}
	public synchronized void setAction(byte[]a)throws Exception{
		/*int len=Math.min(a.length,16);
		char s[]=new char[len*2],ss[]="0123456789ABCDEF".toCharArray();
		for(int i=0;i<len;++i){
			s[i*2]=ss[a[i]&15];
			s[i*2+1]=ss[a[i]>>4&15];
		}
		debug.Log.i(new String(s)+":"+a.length);*/
		
		
		if(!player.online)throw new Exception("Player has been removed");
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
		if(!player.online)return "[offline]";
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
	RemotePlayerInfo(Player pl,String un,String pw){
		super(pl);
		username=un;
		password=pw;
	}
	boolean check(String un,String pw){
		return !player.online&&un.equals(username)&&pw.equals(password);
	}
	boolean check0(String un){return un.equals(username);}
	public String getUserName(){
		return username;
	}
}
