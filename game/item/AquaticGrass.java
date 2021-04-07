package game.item;

import util.BmpRes;
import game.entity.Agent;
import static util.MathUtil.*;
import game.world.World;
import game.block.WaterBlock;
import game.block.AquaticGrassBlock;
import game.block.Block;

public class AquaticGrass extends Item implements BlockItem{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/AquaticGrass");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 8;}
	public int foodVal(){return 4;}
	
	public Item clickAt(double x,double y,Agent a){
		int px=f2i(x),py=f2i(y);
		Block b1=World.cur.get(px,py).rootBlock();
		Block b2=World.cur.get(px,py-1).rootBlock();
		if(b1.getClass()==WaterBlock.class&&(b2.isSolid()||b2.getClass()==AquaticGrassBlock.class)){
			World.cur.place(px,py,new AquaticGrassBlock());
			return null;
		}
		return super.clickAt(x,y,a);
	}

}
