package game.item;

import util.BmpRes;

public class Coal extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Coal");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 100;}
};
