package game.block;

import util.BmpRes;

public class SpringBlock extends IronBasedType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/SpringBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 50;}
	public double getJumpAcc(){return 1.6;}
};
