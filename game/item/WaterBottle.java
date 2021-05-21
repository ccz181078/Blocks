package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.block.*;
import game.world.World;
import game.entity.*;

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
	@Override
	public boolean autoUse(Human h,Agent a){
		if(World.cur.get(rnd(h.left,h.right),rnd(h.top,h.bottom)) instanceof game.block.FireBlock){
			h.clickAt(h.x+h.xdir*(1+1e-4),h.y-1);
			return true;
		}
		return false;
	}
}
