package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;
import game.block.ContactInductorBlock;

public class ContactInductor extends Item{
	private static final long serialVersionUID=1844677L;
	public static BmpRes[] bmp=BmpRes.load("Item/ContactInductor_",2);
	public BmpRes getBmp(){return bmp[0];}

	public Item clickAt(double x,double y,game.entity.Agent a){
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py);
		if(b.circuitCanBePlaced()){
			World.cur.setCircuit(px,py,new ContactInductorBlock(b));
			return null;
		}
		return super.clickAt(x,y,a);
	}
};
