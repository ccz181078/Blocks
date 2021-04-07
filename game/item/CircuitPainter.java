package game.item;

import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;
import game.world.World;
import game.block.*;

public class CircuitPainter extends Tool{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/CircuitPainter");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 0;}
	public int maxDamage(){return 10000;}
	public Item clickAt(double tx,double ty,Agent a){
		if(!(a instanceof Player))return this;
		if(!((Player)a).creative_mode)return this;
		if(new EnergyStone().clickAt(tx,ty,a)==null)++damage;
		return this;
	}
	int x0,y0,dir=-1;
	transient long last_press_time=0;
	public boolean onLongPress(Agent a,double tx,double ty){
		if(!(a instanceof Player))return true;
		if(!((Player)a).creative_mode)return true;
		int px=f2i(tx),py=f2i(ty);
		if(x0==-1&&y0==-1){
			x0=px;y0=py;
		}else{
			int tp=-1;
			if(x0-1==px&&y0==py)tp=0;
			if(x0==px&&y0+1==py)tp=1;
			if(x0+1==px&&y0==py)tp=2;
			if(x0==px&&y0-1==py)tp=3;
			if(tp!=-1){
				if(new OneWayWire(tp).clickAt(x0+0.5,y0+0.5,a)==null)++damage;
				if(dir!=-1&&dir!=tp){
					if(new EnergyStone().clickAt(x0+0.5,y0+0.5,a)==null)++damage;
				}
			}
			x0=px;y0=py;
			if(tp!=-1){
				if(new OneWayWire(tp).clickAt(x0+0.5,y0+0.5,a)==null)++damage;
				dir=tp;
			}
		}
		last_press_time=World.cur.time;
		return true;
	}
	
	public void onCarried(Agent a){
		if(last_press_time+5<World.cur.time)x0=y0=dir=-1;
	}

	@Override
	public boolean isCreative(){return true;}

}
