package game.item;

import util.BmpRes;
import game.entity.Entity;

public class Bullet extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Bullet");
	public BmpRes getBmp(){return bmp;}
	Entity asEnt(){return new game.entity.Bullet(this);}
	public void onKill(double x,double y){}
};
