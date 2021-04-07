package game.item;

import util.BmpRes;
import game.entity.*;
import static util.MathUtil.*;

public class BloodBottle extends Bottle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/BloodBottle");
	public BmpRes getBmp(){return bmp;}
	public void onBroken(double x,double y,game.entity.Bottle ent){
		for(int i=0;i<10;++i){
			new game.entity.BloodBall(x,y,rnd(2,4)).add();
		}
	}
	@Override
	public boolean autoUse(final Human h,final Agent a){
		if(h.maxHp()-h.hp>15&&h.yv<1e-4){
			h.clickAt(h.x+h.xdir*(1+1e-4),h.y-1);
			return true;
		}
		return false;
	}
	@Override
	public void autoLaunch(final Human h,final Agent a){}
}
