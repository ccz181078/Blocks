package game.block;

import util.BmpRes;

public class GoldBlock extends StoneType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/GoldBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 50;}

};
