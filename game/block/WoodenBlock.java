package game.block;

import util.BmpRes;
import game.item.Item;
import game.item.Coal;

public class WoodenBlock extends WoodenType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/WoodenBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 60;}
	public int heatingTime(boolean in_furnace){return 100;}
	public Item heatingProduct(boolean in_furnace){return new Coal();}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
};
