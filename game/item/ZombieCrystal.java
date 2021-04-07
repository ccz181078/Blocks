package game.item;

import util.BmpRes;

public class ZombieCrystal extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/ZombieCrystal");
	public BmpRes getBmp(){return bmp;}
	public int foodVal(){return 15;}
	public int fuelVal(){return 40;}
}
