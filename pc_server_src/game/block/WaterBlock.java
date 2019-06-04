package game.block;

import util.BmpRes;

public class WaterBlock extends LiquidType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/WaterBlock");
	public BmpRes getBmp(){return bmp;}
	public double transparency(){return 0.05;}
	double friction(){return 0.6;}
	int maxDamage(){return 15;}
};
