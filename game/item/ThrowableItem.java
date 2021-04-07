package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public abstract class ThrowableItem extends LaunchableItem implements ShootableTool{
	abstract int energyCost();
	public Item click(double x,double y,Agent a){
		return clickAt(x,y,a);
	}
	public Item clickAt(double x,double y,Agent h){
		y-=h.y;
		x-=h.x+h.dir*h.width();
		int xp=energyCost();
		if(h.xp<xp)return this;
		h.xp-=xp;
		h.throwEnt(toEnt(),y/x,mv2());
		return null;
	}
	public Entity test_shoot(Human h,double x,double y){
		y-=h.y;
		x-=h.x+h.dir*h.width();
		Entity.beginTest();
		h.throwEnt(toEnt(),y/x,mv2());
		return Entity.endTest();
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		return ShootTool.auto(h,a,this);
	}
}