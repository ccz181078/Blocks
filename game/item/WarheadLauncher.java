package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class WarheadLauncher extends EnergyLauncher{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/WarheadLauncher");
	/*
	public boolean onLongPress(Agent a,double tx,double ty){
		y-=a.y+0.2;
		x-=a.x;
		if(abs(x)<=a.width()+0.1)return true;
		if(rnd()>0.3){
			++damage;
			game.entity.Spark ball=new game.entity.Spark(a.x,a.y).setHpScale(4);
			ball.adhesive=true;
			a.throwEnt(ball,(ty-a.y)/(tx-(a.x+a.width()*a.dir)),slope+rnd_gaussion()*0.1,0.2+rnd_gaussion()*0.05);
		}
		return true;
	}*/
	
	SpecialItem<Warhead> warhead=new SpecialItem<Warhead>(Warhead.class);
	int cur_cnt=0;
	Warhead cur=null;
	double rx=0,ry=0;
	int leak_time=0;
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 10000;}
	public int energyCost(){return 1;}
	public boolean shootCond(){
		return cur_cnt>0&&cur!=null&&hasEnergy(energyCost());
	}
	public Entity getBall(){
		if(!Entity.is_test)--cur_cnt;
		if(cur==null)return null;
		return cur.getBall();
	}
	@Override
	public void onCarried(game.entity.Agent a){
		super.onCarried(a);
		if(cur_cnt<=0){
			leak_time=0;
			cur=warhead.popItem();
			if(cur!=null)cur_cnt=cur.getBallCnt();
		}
		if(leak_time>0&&ready()){
			clickAt(a.x+rx*a.dir,a.y+ry,a);
			--leak_time;
		}
	}
	@Override
	public Item clickAt(double x,double y,Agent a){
		rx=(x-a.x)*a.dir;
		ry=y-a.y;
		return super.clickAt(x,y,a);
	}
	
	public void shoot(double s,Agent w){
		super.shoot(s,w);
		resetCd(rndi(1,4));
		leak_time+=rndi(0,7)/3;
	}

	public ShowableItemContainer getItems(){
		return ItemList.create(ec,warhead);
	}
	
	@Override
	public boolean autoUse(Human h,Agent a){
		if(warhead.isEmpty()){
			for(SingleItem si:h.items.toArray())insert(si);
		}
		if(!shootCond())return false;
		return super.autoUse(h,a);
	}
};
