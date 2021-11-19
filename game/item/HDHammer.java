package game.item;

import util.BmpRes;

public class HDHammer extends IronHammer{
	static BmpRes bmp=new BmpRes("Item/HDHammer");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 5;}
	public int maxDamage(){return 10000;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
}

