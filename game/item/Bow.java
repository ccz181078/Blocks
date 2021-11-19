package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class Bow extends Tool implements DefaultItemContainer,ShootableTool{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Bow");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 0;}
	public int maxDamage(){return 100;}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
	SpecialItem<Arrow> arrow=new SpecialItem<Arrow>(Arrow.class);
	int cd=0;
	public Item click(double x,double y,Agent a){
		return clickAt(x,y,a);
	}
	public Item clickAt(double x,double y,Agent w){
		if(cd>0)return this;
		y-=w.y;
		x-=w.x+w.dir*w.width();
		if(!w.hasEnergy(energyCost())||arrow.isEmpty())return this;
		w.loseEnergy(energyCost());
		shoot(y/x,w);
		return this;
	}
	
	@Override
	public void onCarried(game.entity.Agent a){
		if(cd>0)--cd;
	}
	public int energyCost(){return 5;}
	
	public final void shoot(double s,Agent w){
		if(!Entity.is_test){
			cd=16;
			++damage;
			Entity ball=new game.entity.Arrow(arrow.popItem(),this);
			Agent.temp(w,game.entity.SourceTool.use(w,getName())).throwEnt(ball,s,mv2());
		}else{
			w.throwEnt(new game.entity.Arrow(new game.item.Arrow(),this),s,mv2());
		}
	}
	public Entity test_shoot(Human w,double x,double y){
		if(cd>0)return null;
		
		y-=w.y;
		x-=w.x+w.dir*w.width();
		if(arrow.isEmpty())return null;
		
		Entity.beginTest();
		shoot(y/x,w);
		return Entity.endTest();
	}
	
	@Override
	public boolean autoUse(Human h,Agent a){
		if(cd>0)return rnd()<0.5;
		if(arrow.isEmpty()){
			for(SingleItem si:h.items.toArray())insert(si);
		}
		
		if(arrow.isEmpty())return false;
		return ShootTool.auto(h,a,this);
	}

	public ShowableItemContainer getItems(){return arrow;}
};
