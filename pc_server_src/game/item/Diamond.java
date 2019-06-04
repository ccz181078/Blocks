package game.item;

import util.BmpRes;

public class Diamond extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Diamond");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 40;}
};
