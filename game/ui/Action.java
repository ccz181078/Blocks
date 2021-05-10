package game.ui;

import static java.lang.Math.*;
import static util.MathUtil.*;
import java.util.*;
import game.world.World;
import game.entity.Player;
import game.entity.Agent;
import game.item.SingleItem;
import util.*;
import debug.Log;
import static game.world.NearbyInfo.BW;

public class Action implements java.io.Serializable{
private static final long serialVersionUID=1844677L;
	public boolean l,r,u,d;
	public int width,height;
	public int item_sel_group=0,item_sel_index=0;
	public ArrayList<MotionEvent>mes;
	public ArrayList<Short>known_id;
	public ArrayList<Short>unknown_texture;
	public transient HashSet<Short>known_texture;
	public transient float tx,ty;
	public transient boolean on;
	transient long last_press_time;
	public void upd(Action action){
		l=action.l;
		r=action.r;
		u=action.u;
		d=action.d;
		item_sel_group=action.item_sel_group;
		item_sel_index=action.item_sel_index;
		width=action.width;
		height=action.height;
		mes.addAll(action.mes);
		unknown_texture.addAll(action.unknown_texture);
	}
	public byte[] getBytes(){
		try{
			byte[] data=SerializeUtil.obj2bytes(this);
			//Log.i("getBytes  "+data.length);
			mes.clear();
			known_id.clear();
			unknown_texture.clear();
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
		unknown_texture=new ArrayList<>();
		known_texture=new HashSet<>();
	}
	public void addUnknownTexture(short id){
		if(!known_texture.contains(id)){
			known_texture.add(id);
			unknown_texture.add(id);
		}
	}
	public void apply(Player target){
		if(item_sel_group!=0){
			int id=(item_sel_group-1)*4;
			item_sel_group=0;
			target.items.select(target.items.toArray()[id]);
		}
		if(item_sel_index!=0){
			SingleItem a[]=target.items.toArray(),w=target.items.getSelected();
			for(int i=0;i<15;++i){
				if(a[i]==w){
					int id=i/4*4+item_sel_index-1;
					if(id<15)target.items.select(a[id]);
					break;
				}
			}
			item_sel_index=0;
		}
		UI.pl=target;
		Agent agent=target.getControlledAgent();
		agent.xdir=(r?1:0)-(l?1:0);
		agent.ydir=(u?1:0)-(d?1:0);
		final float C=BW*Math.min(1,(height*2f)/width);
		for(MotionEvent m:mes){
			if(m instanceof KeyEvent){
				char c=((KeyEvent)m).c;
				UI.pl.onKey(c);
				continue;
			}
			if(m instanceof TextEvent){
				String s=UI.pl.name+": "+((TextEvent)m).s;
				for(Player p:World.cur.getPlayers()){
					p.addText(s);
				}
				continue;
			}
			if(m.tp>=3){
				tx=(m.x-width/2)/(height/C);
				ty=(height/2-m.y)/(height/C);
				if(m.tp==3){
					double rot=target.getRotation()*PI/180;
					double tx0=tx,ty0=ty,c=cos(rot),s=sin(rot);
					tx=(float)(tx0*c+ty0*s);
					ty=(float)(-tx0*s+ty0*c);
					target.setDes(target.x+tx,target.y+ty);
				}
				continue;
			}
			//Log.i("ME apply  "+m.x+","+m.y+":"+m.tp+":"+World.cur.time);
			//Log.i("touch time  "+":"+(World.cur.time-last_press_time));
			if(target.UI_pressAt((m.x-width)/(height/8),m.y/(height/8),m.tp)){
				on=false;
				continue;
			}else if(target.closeDialog())continue;
			tx=(m.x-width/2)/(height/C);
			ty=(height/2-m.y)/(height/C);
			if(true){
				double rot=target.getRotation()*PI/180;
				double tx0=tx,ty0=ty,c=cos(rot),s=sin(rot);
				tx=(float)(tx0*c+ty0*s);
				ty=(float)(-tx0*s+ty0*c);
			}
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
		target.setCursorState(on,tx,ty,World.cur.time-last_press_time);
//		if(on&&World.cur.time-last_press_time>12)target.setDes(f2i(target.x+tx),f2i(target.y+ty));
//		else target.cancelDes();
	}
	public void onKey(char key){
		mes.add(new KeyEvent(key));
	}
	public void onTouch(float x,float y,int type){
		//Log.i("onTouch  "+x+","+y+":"+type);
		mes.add(new MotionEvent(x,y,type));
	}
	public void onTouch(float x,float y,int type,int is_left_click){
		if(is_left_click!=0)onTouch(x,y,type);
		else onTouch(x,y,type+3);
	}
	public void sendText(String text){
		mes.add(new TextEvent(text));
	}
}
class KeyEvent extends MotionEvent{
	char c;
	KeyEvent(char _c){
		super(0,0,-2);
		c=_c;
	}
}
class TextEvent extends MotionEvent{
	private static final long serialVersionUID=1844677L;
	String s;
	TextEvent(String _s){
		super(0,0,-1);
		s=_s;
	}
}
class MotionEvent implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	float x,y;
	int tp;
	MotionEvent(float _x,float _y,int _tp){
		x=_x;y=_y;tp=_tp;
	}
}
