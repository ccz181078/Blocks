package game.item;

import util.BmpRes;

public class DiamondDrill extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DiamondDrill");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 30000;}
	public double toolVal(){return 15;}
	public double hardness(){return game.entity.NormalAttacker.DIAMOND;}
};
