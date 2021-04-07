package game.block;

import util.BmpRes;

public class IronBlock extends PureStoneType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/IronBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 240;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
};
