package game.item;

import util.BmpRes;
import game.block.WireBlock;
import game.world.World;
import game.block.Block;
import static util.MathUtil.*;

public class EnergyStone extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyStone");
	public BmpRes getBmp(){return bmp;}
	public Item clickAt(double x,double y,game.entity.Agent a){
		Block b=World._.get(x,y);
		if(b.getClass()==WireBlock.class){
			if(((WireBlock)b).ins(f2i(x),f2i(y),WireBlock.mid,WireBlock.mid))return null;
		}
		return super.clickAt(x,y,a);
	}
};
