package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;
import game.block.*;
import game.entity.Source;

public class Iron extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Iron");
	public BmpRes getBmp(){return bmp;}
	@Override
	public void onVanish(double x,double y,Source src){
		new IronOre().drop(x,y);
	}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
}
