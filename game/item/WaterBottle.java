package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.block.*;
import game.world.World;

public class WaterBottle extends Bottle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/WaterBottle");
	public BmpRes getBmp(){return bmp;}
	public void onBroken(double x,double y,game.entity.Bottle ent){
		for(int i=0;i<100;++i){
			BlockAt b=World.cur.get1(f2i(x+rnd_gaussion()*3),f2i(y+rnd_gaussion()*3));
			if(b.block instanceof FireBlock){
				World.cur.setAir(b.x,b.y);
			}
		}
	}
}
