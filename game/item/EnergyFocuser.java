package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class EnergyFocuser extends EnergyTool{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyFocuser");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 0;}
	public void onDesBlock(Block b){}
	public int maxDamage(){return 5000;}
	int cd=0;
	@Override
	public void onCarried(Agent a){
		if(cd>0)--cd;
	}
	@Override
	public Item clickAt(double tx,double ty,Agent a){
		onLongPress(a,tx,ty);
		return this;
	}
	@Override
	public boolean onLongPress(Agent a,double tx,double ty){
		if(abs(tx-a.x)>4||abs(ty-a.y)>4)return false;
		if(hasEnergy(200)&&cd<=0){
			cd=4;
			loseEnergy(200);
			++damage;
			new FocusedEnergy().initPos(tx,ty,a.xv,a.yv,SourceTool.make(a,"放置的")).add();
		}
		return true;
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(cd>0)return false;
		if(!findEnergyCell(h,200))return false;
		double D=h.distLinf(a);
		if(2<D&&D<4){
			h.clickAt(a.x,a.y);
			return true;
		}
		return false;
	}
}