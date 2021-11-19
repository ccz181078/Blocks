package game.item;

import static java.lang.Math.*;
import util.BmpRes;
import game.entity.*;
import game.world.World;

public class MeteoriteGenerator extends EnergyLauncher{
	static BmpRes bmp=new BmpRes("Item/MeteoriteGenerator");
	public BmpRes getBmp(){return bmp;}
	public int energyCost(){return 40;}
	SingleItem rpg=new OtherItem();
	SpecialItem<DarkBall> dark_ball=new SpecialItem<>(DarkBall.class);
	public Item clickAt(double x,double y,Agent w){
		w.dir=(x-w.x>0?1:-1);
		if(!ready())return this;
		if(!shootCond())return this;
		if(max(abs(x-w.x),abs(y-w.y))>4)return this;
		if(dark_ball.isEmpty())return this;
		dark_ball.popItem();
		Item r = rpg.popItem();
		
		resetCd();
		loseEnergy(energyCost());
		++damage;
		
		Entity e=r.asEnt();
		e.initPos(x,World.cur.getMaxY()+1-e.height()-0.01,0,-1e-3,SourceTool.useToLaunch(w.getLaunchSrc(),getName()));
		e.add();
		return this;
	}
	public ShowableItemContainer getItems(){
		return ItemList.create(ec,rpg,dark_ball);
	}
	
	@Override
	public boolean shootCond(){return hasEnergy(energyCost())&&!rpg.isEmpty();}
	
	@Override
	public Entity test_shoot(Human w,double x,double y){
		if(rpg.isEmpty())return null;
		return test_shoot(w,x,y,rpg.get().clone());
	}
	public Entity test_shoot(Human w,double x,double y,Item r){
		if(!ready())return null;
		if(abs(x-w.x)>4)return null;
		Entity e=rpg.get().asEnt();
		return e.initPos(x,World.cur.getMaxY()+1-e.height(),0,0,null);
	}
	
	public Entity getBall(){return rpg.popItem().asEnt();}

	@Override
	public boolean autoShoot(Human h,Agent a){
		h.items.insert(rpg);
		if(!ready())return false;
		if(abs(h.x-a.x)>4||abs(h.xv)>0.1)return false;
		if(rpg.isEmpty()){
			for(SingleItem si:h.items.toArray()){
				Item i=si.get();
				if(i instanceof StoneBall){
					rpg.insert(i);
					break;
				}
			}
		}
		if(rpg.isEmpty())return false;
		h.clickAt(a.x,a.y);
		return true;
	}
	
}
