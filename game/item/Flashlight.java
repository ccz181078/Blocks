package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.Agent;

public class Flashlight extends EnergyTool{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Flashlight");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	public double toolVal(){return 0;}
	@Override
	public double light(){return hasEnergy(1)?2:0;}
	@Override
	public void onCarried(Agent a){
		if(hasEnergy(1)){
			if(rnd()<0.1)loseEnergy(1);
			if(rnd()<0.03)++damage;
		}
	}
}

