package game.item;

import util.BmpRes;

public class DiamondShovel extends IronShovel{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DiamondShovel");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 5;}
	public int maxDamage(){return 25000;}
}

