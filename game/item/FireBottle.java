package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.block.*;
import game.world.World;
import game.entity.*;

public class FireBottle extends Bottle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/FireBottle");
	public BmpRes getBmp(){return bmp;}
	public void onBroken(double x,double y,game.entity.Bottle ent){
		Spark.explode_adhesive(x,y,0,0,1,0,25,ent);
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		return ShootTool.auto(h,a,this);
	}
}
