package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;
import static game.ui.UI.drawProgressBar;

public class AutoDefenceShoes extends EnergyShoes{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/SpringShoes");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	SpecialItem<Bullet> bullet=new SpecialItem<>(Bullet.class);
	public ShowableItemContainer getItems(){return ItemList.create(ec,bullet);}
	
	@Override
	public Attack transform(Attack a){
		return super.transform(a);
	}
	
	@Override
	public double onImpact(Human h,double v){
		damage+=rf2i(v);
		return max(0,(v-60)*0.2);
	}
	
	double state=1;
	
	@Override
	public Shoes update(Human h){
		state=max(0,min(1,state+0.1));
		if(rnd()<state)
		for(Entity e:World.cur.getNearby(h.x,h.y,h.width()+3,h.height()+3,false,true,false).ents){
			if(e.harmless())continue;
			if(e.isPureEnergy())continue;
			if(e.width()>0.42||e.height()>0.42)continue;
			if(!hasEnergy(1))continue;
			if(bullet.isEmpty())continue;
			double xd=e.x+e.xv-h.x,yd=e.y+e.yv-h.y;
			double s1=max(1,hypot(e.xv,e.yv)),s2=max(1,hypot(h.xv,h.yv));
			double rxv=e.xv/s1-h.xv/s2,ryv=e.yv/s1-h.yv/s2;
			if(xd*rxv+yd*ryv<0&&e.getSrc()!=h&&hypot(rxv,ryv)>0.4){
				double t=max(0,hypot(xd,yd)-0.5)/(hypot(rxv,ryv)+1e-2);
				if(t<3&&abs(e.x+e.xv-h.x)<h.width()+1.5+e.width()&&abs(e.y+e.yv-h.y)<h.height()+1.5+e.height()){
					loseEnergy(1);
					Entity z=bullet.popItem().asEnt();
					z.thrownFrom(h,e.x+e.xv,e.y+e.yv,-e.xv,-e.yv);
					state*=0.6;
					if(rnd()>state)break;
				}
			}
		}
		return super.update(h);
	}

};
