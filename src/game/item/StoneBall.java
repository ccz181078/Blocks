package game.item;

import util.BmpRes;
import game.entity.Agent;
import static java.lang.Math.*;

public class StoneBall extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/StoneBall");
	public BmpRes getBmp(){return bmp;}
	public game.entity.StoneBall getBall(){
		return new game.entity.StoneBall();
	}
	public Item clickAt(double x,double y,Agent a){
		y-=a.y;
		x-=a.x;
		if(x*a.dir<=a.width()||a.xp<10)return this;
		a.xp-=10;
		a.throwEnt(getBall(),y/x,0.2);
		return null;
	}
};
