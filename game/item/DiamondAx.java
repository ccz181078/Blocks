package game.item;

import util.BmpRes;

public class DiamondAx extends IronAx{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DiamondAx");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 5;}
	public int maxDamage(){return 25000;}
	public double hardness(){return game.entity.NormalAttacker.DIAMOND;}
}

