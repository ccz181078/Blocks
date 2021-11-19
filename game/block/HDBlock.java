package game.block;

import util.BmpRes;

public class HDBlock extends PureStoneType{
	static BmpRes bmp=new BmpRes("Block/HDBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 240*4;}
	public int mass(){return 40;}
	public int shockWaveResistance(){return 10000;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
};

