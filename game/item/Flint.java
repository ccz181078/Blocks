package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.World;
import static util.MathUtil.*;

public class Flint extends Tool{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Flint");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 0;}
	public Item clickAt(double x,double y,Agent a){
		World.cur.get(x,y).onFireUp(f2i(x),f2i(y));
		++damage;
		return this;
	}
	public void onDesBlock(Block b){}
	public int maxDamage(){return 50;}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
}
