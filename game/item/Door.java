package game.item;

import util.BmpRes;
import game.entity.*;
import game.world.World;
import game.block.Block;
import game.block.DoorBlock;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class Door extends Item implements BlockItem{
	public static BmpRes bmp=new BmpRes("Block/DoorBlock");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public Item clickAt(double x,double y,Agent a){
		Block b=World.cur.get(x,y);
		int px=f2i(x),py=f2i(y);
		if(b.circuitCanBePlaced()){
			DoorBlock w=new DoorBlock(b);
			World.cur.setCircuit(px,py,w);
			return null;
		}
		return super.clickAt(x,y,a);
	}
};
