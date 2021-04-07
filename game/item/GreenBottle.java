package game.item;

import util.BmpRes;
import game.block.*;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;

public class GreenBottle extends Bottle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/GreenBottle");
	public BmpRes getBmp(){return bmp;}
	public void onBroken(double x,double y,game.entity.Bottle ent){
		new game.entity.GreenBall().setPos(x,y).add();
	}
	@Override
	public boolean autoUse(final Human h,final Agent a){
		if(h.maxHp()-h.hp>5&&h.yv<1e-4){
			h.clickAt(h.x+h.xdir*(1+1e-4),h.y-1);
			return true;
		}
		return false;
	}
	@Override
	public void autoLaunch(final Human h,final Agent a){}
}
