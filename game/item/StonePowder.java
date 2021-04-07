package game.item;

import util.BmpRes;

public class StonePowder extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/StonePowder");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
}

