package game.item;

import util.BmpRes;
import game.entity.Agent;
import game.block.DirtBlock;
import game.world.World;
import static util.MathUtil.*;
import game.block.AirBlock;
import game.block.GrassBlock;

public class Grass extends Item implements BlockItem{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Grass");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 5;}
	public int foodVal(){return 4;}

	public Item clickAt(double x,double y,Agent a){
		int px=f2i(x),py=f2i(y);
		if(World.cur.get(px,py-1).isSolid()){
			if(World.cur.get(px,py).getClass()==AirBlock.class){
				World.cur.place(px,py,new GrassBlock(0));
				return null;
			}
		}
		return super.clickAt(x,y,a);
	}
	
};
