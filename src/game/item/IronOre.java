package game.item;

import util.BmpRes;

public class IronOre extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/IronOre");
	public BmpRes getBmp(){return bmp;}
	public int heatingTime(boolean in_furnace){return in_furnace?200:1000000000;}
	public Item heatingProduct(boolean in_furnace){return in_furnace?new Iron():air();}
};
