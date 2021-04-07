package game.item;

import util.BmpRes;

public class StringItem extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/String");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 3;}
	
};
