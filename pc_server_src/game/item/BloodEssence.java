package game.item;

import util.BmpRes;

public class BloodEssence extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BloodEssence");
	public BmpRes getBmp(){return bmp;}
	public int foodVal(){return 30;}
	public int fuelVal(){return 80;}
	public int heatingTime(boolean in_furnace){return 20;}
	public Item heatingProduct(boolean in_furnace){return new CarbonPowder();}
};
