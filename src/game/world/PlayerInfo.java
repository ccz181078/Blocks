package game.world;

import static util.SerializeUtil.*;
import game.entity.Player;
import java.util.BitSet;
import game.ui.Action;
import android.util.Log;
import game.GameSetting;

public class PlayerInfo implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	volatile Player player;
	transient float H_div_W=0;
	public transient volatile GameSetting game_setting;
	transient volatile byte[] ni=null;
	transient volatile byte[] action=null;
	transient volatile long last_remove_time=0;
	transient int skip_t;
	transient BitSet known_id=new BitSet(32768);
	public void add(){
		if(!player.removed)return;
		skip_t=0;
		player.removed=false;
		if(last_remove_time!=World._.time)player.add();
	}
	public void remove(){
		if(player.removed)return;
		if(known_id!=null)known_id.clear();
		action=ni=null;
		player.removed=true;
		last_remove_time=World._.time;
	}
	void readAction(){
		if(player.removed)return;
		if(action!=null){
			try{
				Action a=(Action)bytes2obj(action);
				H_div_W=a.height/(a.width+1e-3f);
				for(short x:a.known_id)known_id.set(x);
				player.action.upd(a);
			}catch(Exception e){e.printStackTrace();}
			action=null;
			skip_t=0;
		}else ++skip_t;
	}
	void genNI(){
		if(player.removed)return;
		ni=World._.getNearby(player).getBytes((BitSet)known_id.clone(),game_setting,H_div_W);
	}
	public void setAction(byte[]a)throws Exception{
		if(player.removed)throw new Exception("Player has been removed");
		action=a;
	}
	public byte[] getNI(){
		byte[] w=ni;
		ni=null;
		return w;
	}
	boolean check(String un,String pw){return false;}
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
}
