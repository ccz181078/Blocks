package game.block;

import util.BmpRes;

public class DarkSandBlock extends DirtType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/DarkSandBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 40;}
	
};
