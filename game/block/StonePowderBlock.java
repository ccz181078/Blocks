package game.block;

import util.BmpRes;

public class StonePowderBlock extends DirtType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/StonePowderBlock");
	public BmpRes getBmp(){return bmp;}
	@Override
	public int shockWaveResistance(){return super.shockWaveResistance()*10;}
	int maxDamage(){return 100;}
}
