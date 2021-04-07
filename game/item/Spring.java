package game.item;

import util.BmpRes;

public class Spring extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Spring");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
};

