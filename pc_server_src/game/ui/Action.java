package game.ui;

import static java.lang.Math.*;
import static util.MathUtil.*;
import java.util.*;
import game.world.World;
import game.entity.Player;
import util.*;
import debug.Log;
import static game.world.NearbyInfo.BW;

public class Action implements java.io.Serializable{
private static final long serialVersionUID=1844677L;
	public boolean l,r,u,d;
	public int width,height;
	public ArrayList<MotionEvent>mes;
	public ArrayList<Short>known_id;
	public transient float tx,ty;
	public transient boolean on;
	transient long last_press_time;
	public void upd(Action action){
		l=action.l;
		r=action.r;
		u=action.u;
		d=action.d;
		width=action.width;
		height=action.height;
		mes.addAll(action.mes);
	}
	public byte[] getBytes(){
		try{
			byte[] data=SerializeUtil.obj2bytes(this);
			//Log.i("getBytes  "+data.length);
			mes.clear();
			known_id.clear();
			return data;
		}catch(Exception e){e.printStackTrace();}
		return null;
	}
	public Action(int W,int H){
		width=W;
		height=H;
		init();
	}
	public void init(){
		mes=new ArrayList<>();
		known_id=new ArrayList<>();		
	}
	public void apply(Player target){
		UI.pl=target;
		target.xdir=(r?1:0)-(l?1:0);
		target.ydir=(u?1:0)-(d?1:0);
		for(MotionEvent m:mes){
			if(m instanceof TextEvent){
				String s=UI.pl.name+": "+((TextEvent)m).s;
				for(Player p:World.cur.getPlayers()){
					p.addText(s);
				}
				continue;
			}
			//Log.i("ME apply  "+m.x+","+m.y+":"+m.tp+":"+World.cur.time);
			//Log.i("touch time  "+":"+(World.cur.time-last_press_time));
			if(m.tp==0){
				if(target.UI_pressAt((m.x-width)/(height/8),m.y/(height/8))){
					on=false;
					continue;
				}else if(target.closeDialog())continue;
			}
			tx=(m.x-width/2)/(height/BW);
			ty=(height/2-m.y)/(height/BW);
			if(m.tp==0){
				last_press_time=World.cur.time;
				on=true;
			}else if(m.tp==1){
				if(World.cur.time-last_press_time<=10){
					target.clickAt(target.x+tx,target.y+ty);
				}
				on=false;
			}
		}
		mes.clear();
		target.setCursorState(on,tx,ty);
		if(on&&World.cur.time-last_press_time>12)target.setDes(f2i(target.x+tx),f2i(target.y+ty));
		else target.cancelDes();
	}
	public void onTouch(float x,float y,int type,long time){
		//Log.i("onTouch  "+x+","+y+":"+type);
		mes.add(new MotionEvent(x,y,type,time));
	}
	public void sendText(String text){
		mes.add(new TextEvent(text));
	}
}
class TextEvent extends MotionEvent{
	private static final long serialVersionUID=1844677L;
	String s;
	TextEvent(String _s){
		super(0,0,-1,0);
		s=_s;
	}
}
class MotionEvent implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	float x,y;
	int tp;
	MotionEvent(float _x,float _y,int _tp,long _time){
		x=_x;y=_y;tp=_tp;
	}
}
