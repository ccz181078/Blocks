package game.item;

import util.BmpRes;

public class Apple extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Apple");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 5;}
	public int foodVal(){return 8;}
};
