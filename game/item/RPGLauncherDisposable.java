package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class RPGLauncherDisposable extends Tool implements ShootableTool,DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPGLauncherDisposable");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1;}
	public double toolVal(){return 0;}
	public double mv2(){return EnergyTool.E0*20;}
	SingleItem rpg=new NonOverlapOtherItem();
	
	public Item click(double x,double y,Agent a){
		return clickAt(x,y,a);
	}
	public Item clickAt(double x,double y,Agent w){
		if(!shootCond())return this;
		if(!w.hasEnergy(10))return this;
		w.loseEnergy(10);
		
		Item r = rpg.popItem();

		y-=w.y;
		x-=w.x+w.dir*w.width();
		
		++damage;
		r.onLaunch(w,y/x,mv2());
		return this;
	}
	
	public boolean shootCond(){return damage==0&&!rpg.isEmpty();}
	
	public Entity test_shoot(Human w,double x,double y){
		if(rpg.isEmpty())return null;
		return test_shoot(w,x,y,rpg.get().clone());
	}
	public Entity test_shoot(Human w,double x,double y,Item r){
		y-=w.y;
		x-=w.x+w.dir*w.width();
		
		Entity.beginTest();
		r.onLaunch(w,y/x,mv2());
		return Entity.endTest();
	}

	public boolean autoShoot(Human h,Agent a){
		h.items.insert(rpg);
		if(rpg.isEmpty())RPGLauncher.selectLaunchableItem(h,rpg);
		if(rpg.isEmpty())return false;
		Item it=rpg.get();
		if(
		  it instanceof LaunchableItem
		||it instanceof game.block.Block){
			boolean ret=ShootTool.auto(h,a,this);
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
		return rpg;
	}
};
