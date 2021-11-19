package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class Slingshot extends Tool implements DefaultItemContainer,ShootableTool{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Slingshot");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 0;}
	public int maxDamage(){return 40;}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
	SingleItem item=new OtherItem();
	public ShowableItemContainer getItems(){return item;}
	int cd=0;
	public Item click(double x,double y,Agent a){
		return clickAt(x,y,a);
	}
	public int energyCost(){return 5;}
	public Item clickAt(double x,double y,Agent w){
		if(cd>0)return this;
		y-=w.y;
		x-=w.x;
		if(!w.hasEnergy(energyCost())||item.isEmpty())return this;
		w.loseEnergy(energyCost());
		shoot(y/x,w);
		return this;
	}
	@Override
	public void onCarried(game.entity.Agent a){
		if(cd>0)--cd;
	}
	
	public final void shoot(double s,Agent w){
		if(!Entity.is_test){
			cd=16;
			++damage;
			Entity ball=new ThrowedItem(0,0,item.popItem());
			Agent.temp(w,game.entity.SourceTool.use(w,getName())).throwEnt(ball,s,mv2());
		}else{
			w.throwEnt(new ThrowedItem(0,0,item.get().clone()),s,mv2());
		}
	}
	public Entity test_shoot(Human w,double x,double y){
		if(cd>0)return null;
		
		y-=w.y;
		x-=w.x+w.dir*w.width();
		if(item.isEmpty())return null;
		
		Entity.beginTest();
		shoot(y/x,w);
		return Entity.endTest();
	}

	
	@Override
	public boolean autoUse(Human h,Agent a){
		if(cd>0)return rnd()<0.5;
		if(item.isEmpty()){
			SingleItem ss[]=h.items.toArray();
			for(int t=0;t<30;++t){
				int id=rndi(0,ss.length-1);
				SingleItem si=ss[id];
				if(si.isEmpty())continue;
				Item it=si.get();
				if(it instanceof EnergyTool)continue;
				if(it.swordVal()<3)continue;
				if(it.foodVal()>0)continue;
				item.insert(si);
				break;
			}
		}
		if(item.isEmpty())return false;
		return ShootTool.auto(h,a,this);
	}
	

};
