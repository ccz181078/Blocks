package game.item;

import util.BmpRes;
import game.world.World;
import game.block.DirtBlock;
import game.entity.Source;
import static util.MathUtil.*;

public class Apple extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Apple");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 5;}
	public int foodVal(){return 8;}
	
	@Override
	public void onVanish(double x,double y,Source src){
		if(rnd()<0.2){
			int px=f2i(x),py=f2i(y)-1;
			if(World.cur.get(px,py).rootBlock() instanceof DirtBlock){
				DirtBlock.genTree(px,py);
			}
		}
	}
};
