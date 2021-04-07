package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;
import game.block.EnergySourceBlock;

public class EnergySource extends Item implements BlockItem{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/EnergySource");
	public BmpRes getBmp(){return bmp;}

	public Item clickAt(double x,double y,game.entity.Agent a){
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py);
		if(b.circuitCanBePlaced()){
			World.cur.setCircuit(px,py,new EnergySourceBlock(b));
			return null;
		}
		return super.clickAt(x,y,a);
	}
};
