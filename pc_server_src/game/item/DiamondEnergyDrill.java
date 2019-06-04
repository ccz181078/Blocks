package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import game.entity.Agent;

public class DiamondEnergyDrill extends EnergyDrill{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DiamondEnergyDrill");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 20000;}
	public double toolVal(){return 15;}
	public void onBroken(double x,double y){
		//...
	}
}

