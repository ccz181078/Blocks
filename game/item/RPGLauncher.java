package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class RPGLauncher extends EnergyLauncher{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPGLauncher");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 100;}
	
	public int energyCost(){return 15;}
	
	class MySingleItem extends SingleItem{
		//private static final long serialVersionUID=1844677L;
		@Override
		protected boolean insertable(Item it){
			if(it instanceof RPGLauncher)return false;
			return super.insertable(it);
		}
	}
	SingleItem rpg=new OtherItem();
	public Item clickAt(double x,double y,Agent w){
		w.dir=(x-w.x>0?1:-1);
		if(!ready())return this;
		if(!shootCond())return this;
		Item r = rpg.popItem();
		
		//System.out.println((x-a.x)+","+y+":"+a+":"+r.getClass()+"  cd="+cd);

		y-=w.y;
		x-=w.x+w.dir*w.width();
		
		resetCd();
		loseEnergy(energyCost());
		++damage;
		r.onLaunch(w,y/x,mv2());
		return this;
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
		
		y-=w.y;
		x-=w.x+w.dir*w.width();
		
		Entity.beginTest();
		r.onLaunch(w,y/x,mv2());
		return Entity.endTest();
	}

	@Override
	public boolean autoShoot(Human h,Agent a){
		h.items.insert(rpg);
		if(!ready())return false;
		if(rpg.isEmpty()){
			SingleItem ss[]=h.items.toArray();
			for(int t=0;t<30;++t){
				int id=rndi(0,ss.length-1);
				SingleItem si=ss[id];
				if(si.isEmpty())continue;
				Item it=si.get();
				if(it instanceof RPGLauncher)continue;
				if(it.foodVal()>0)continue;
				if(it instanceof LaunchableItem||it instanceof Warhead){
					rpg.insert(si);
					break;
				}
			}
		}
		if(rpg.isEmpty())return false;
		Item it=rpg.get();
		if(
		  it instanceof LaunchableItem
		||it instanceof game.block.Block){
			boolean ret=super.autoShoot(h,a);
			//System.err.println("launch: "+it+"  "+ret);
			h.items.insert(rpg);
			return ret;
		}
		if(it instanceof Warhead){
			double d=h.distL2(a);
			if(d>2&&d<5&&((Warhead)it).explodeDirected()){
				h.clickAt(a.x,a.y);
				h.items.insert(rpg);
				return true;
			}
		}
		return false;
	}
	
	/*@Override
	public void drawTip(graphics.Canvas cv,Player pl){
		if(!hasEnergy(energyCost())||rpg.isEmpty())return;
		float tx=pl.action.tx,ty=pl.action.ty;
		cv.drawLines(new float[]{(float)(pl.width()*pl.dir),0.2f,tx,ty},0xff|pl.press_t<<24);
	}*/
	public ShowableItemContainer getItems(){
		return ItemList.create(ec,rpg);
	}
	
	public Entity getBall(){return null;}
};
