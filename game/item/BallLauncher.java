package game.item;

import static java.lang.Math.*;
import util.BmpRes;
import game.entity.*;

public class BallLauncher extends EnergyLauncher{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BallLauncher");
	public BmpRes getBmp(){return bmp;}
	SpecialItem<StoneBall> ball=new SpecialItem<StoneBall>(StoneBall.class);
	public int maxDamage(){return 1000;}
	public int energyCost(){return 5;}
	public boolean shootCond(){
		return !ball.isEmpty()&&hasEnergy(energyCost());
	}
	public Entity getBall(){
		StoneBall w=ball.popItem();
		if(w==null)return null;
		return w.toEnt();
	}

	public ShowableItemContainer getItems(){
		return ItemList.create(ec,ball);
	}
	
	int dir=1;
	
	@Override
	public void onCarried(Agent a){
		super.onCarried(a);
		dir=a.dir;
	}
	@Override
	public void shoot(double s,Agent w){
		if(!Entity.is_test){
			resetCd();
			loseEnergy(energyCost());
			++damage;
		}
		Entity e=getBall();
		w.throwEnt(e,s,1e-4);
		e.av-=dir*sqrt(mv2()/e.I());
	}
	
	@Override
	public boolean autoUse(Human h,Agent a){
		if(ball.isEmpty()){
			for(SingleItem si:h.items.toArray())insert(si);
		}
		if(ball.isEmpty())return false;
		return super.autoUse(h,a);
	}
};
