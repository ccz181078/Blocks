package game.item;

import util.BmpRes;
import game.entity.Agent;
import static util.MathUtil.*;
import game.world.World;
import game.block.WaterBlock;
import game.block.AlgaeBlock;

public class Algae extends Item implements BlockItem{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/Algae");
	public BmpRes getBmp(){return bmp;}

	public int fuelVal(){return 5;}
	public int foodVal(){return 3;}

	public Item clickAt(double x,double y,Agent a){
		int px=f2i(x),py=f2i(y);
		if(World.cur.get(px,py).rootBlock().getClass()==WaterBlock.class){
			World.cur.place(px,py,new AlgaeBlock());
			return null;
		}
		return super.clickAt(x,y,a);
	}
	
}
