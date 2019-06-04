package game.block;

import util.BmpRes;

public class StonePowderBlock extends DirtType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/StonePowderBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 100;}
}
