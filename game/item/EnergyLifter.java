package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class EnergyLifter extends EnergyTool{
	static BmpRes bmp=new BmpRes("Item/EnergyLifter");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 0;}
	public void onDesBlock(Block b){}
	public int maxDamage(){return 2000;}
	@Override
	public boolean onLongPress(Agent a,double tx,double ty){
		if(hasEnergy(1)){
			double xd=tx-a.x,yd=ty-a.y,d=hypot(xd,yd)+1e-8;
			xd/=d;yd/=d;
			double x=a.x+(a.width()+0.32)*a.dir,y=a.y;
			NearbyInfo ni=World.cur.getNearby(x,y,0.3,0.3,false,true,true);
			for(Entity e:ni.ents){
				if(!e.harmless())apply(a,e,x,y,xd,yd);
			}
			for(Agent e:ni.agents)apply(a,e,x,y,xd,yd);
		}
		return true;
	}
	
	private void apply(Entity e1,Entity e2,double x,double y,double xdir,double ydir){
		if(hasEnergy(1)){
			double mm=min(e1.mass(),e2.mass());
			if(xdir*(e2.xvAt(x,y)-e1.xv)+ydir*(e2.yvAt(x,y)-e1.yv)>0.3)return;
			double m=min(mm,0.2);
			e1.impulse(x,y,xdir,ydir,-m);
			e2.impulse(x,y,xdir,ydir,m);
			loseEnergy(1);
		}
	}
}
