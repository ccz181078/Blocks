package game.block;

import util.BmpRes;
import game.item.Item;

public class SandBlock extends DirtType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/SandBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 30;}
	public int heatingTime(boolean in_furnace){return 200;}
	public Item heatingProduct(boolean in_furnace){return new GlassBlock();}

};
