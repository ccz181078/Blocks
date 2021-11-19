package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import game.entity.Agent;

public class HDEnergyDrill extends EnergyDrill{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/HDEnergyDrill");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 50000;}
	public double toolVal(){return 31;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	public void onBroken(double x,double y){
		super.onBroken(x,y);
	}
}

