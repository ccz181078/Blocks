package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class EnergyStoneSword extends EnergyTool{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp = new BmpRes("Item/EnergyStoneSword");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	int s=0;
	@Override
	public int swordVal(){return 2;}
	@Override
	public void onAttack(Entity e,Source src){
		if(s>0&&(e instanceof Agent)){
			e.onAttackedByEnergy(s/4,src);
			e.onAttacked(s/4,src,this);
			s/=2;
		}
		++damage;
	}
	@Override
	public boolean onLongPress(Agent a,double x,double y){
		if(hasEnergy(10)){
			if(rnd()*sqrt(s)<10)s+=10;
			loseEnergy(10);
		}
		return super.onLongPress(a,x,y);
	}
	public void onBroken(double x,double y){
		//...
		super.onBroken(x,y);
	}
}

