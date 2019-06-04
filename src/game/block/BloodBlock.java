package game.block;

import util.BmpRes;
import static util.MathUtil.*;

public class BloodBlock extends WoodenType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/BloodBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 100;}
	public int fuelVal(){return 1280;}
};
