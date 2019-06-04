package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;
import game.block.SwitchBlock;

public class Switch extends Item{
	private static final long serialVersionUID=1844677L;
	public static BmpRes[] bmp=BmpRes.load("Item/Switch_",2);
	public BmpRes getBmp(){return bmp[0];}

	public Item clickAt(double x,double y,game.entity.Agent a){
		int px=f2i(x),py=f2i(y);
		Block b=World._.get(px,py);
		if(b.circuitCanBePlaced()){
			World._.setCircuit(px,py,new SwitchBlock(b));
			return null;
		}
		return super.clickAt(x,y,a);
	}
	
};
