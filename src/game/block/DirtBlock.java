package game.block;

import util.BmpRes;

public class DirtBlock extends DirtType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/DirtBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 40;}
};
