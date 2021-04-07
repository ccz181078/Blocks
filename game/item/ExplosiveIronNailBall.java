package game.item;

import util.BmpRes;
import game.entity.*;

public class ExplosiveIronNailBall extends StoneBall{
private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/ExplosiveIronNailBall");
	public BmpRes getBmp(){return bmp;}
	public Entity toEnt(){
		return new game.entity.ExplosiveIronNailBall();
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(h.distL2(a)<3)return false;
		return super.autoUse(h,a);
	}
};
