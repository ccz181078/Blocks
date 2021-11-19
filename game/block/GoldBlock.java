package game.block;

import util.BmpRes;

public class GoldBlock extends PureStoneType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/GoldBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 50;}
	public int mass(){return 4;}
	public double hardness(){return game.entity.NormalAttacker.GOLD;}

};
