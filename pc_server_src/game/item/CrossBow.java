package game.item;

import util.BmpRes;
import game.entity.*;

public class CrossBow extends Bow{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/CrossBow");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 400;}
	public double shootSpeed(){return 0.3;}
};
