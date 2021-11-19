package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class EnergyBoxingGlove extends EnergyTool{
	private static final long serialVersionUID=1844677L;
	int cd=0;
	double a=0;
	private static BmpRes bmp[] = BmpRes.load("Item/EnergyBoxingGlove_",2);
	public BmpRes getBmp(){return bmp[ cd <= 4 ? 0 : 1 ];}
	public int maxDamage(){return 160;}
	public boolean disableRecover(){return true;}
	@Override
	public void onAttack(Entity e,Source src){
		if(cd<=0&&hasEnergy(5)&&(a==a)){
			cd=16;
			//e.onAttackedByEnergy(5,src);
			e.impulseMerge(cos(a)*11,sin(a)*11,0.1);
			super.onAttack(e,src);
			if(e instanceof Human){
				Human h=((Human)e);
				Item w=h.getCarriedItem().popItem();
				if(w!=null){
					Entity t=new ThrowedItem(0,0,w);
					t.initPos(h.x+h.dir*(h.width()+0.3),h.y,e.xv+e.xa+rnd_gaussion()*0.04,e.yv+e.ya+rnd_gaussion()*0.4,SourceTool.hitFromHand(src,h.getName()));
					t.add();
				}
			}
			loseEnergy(5);
		}
	}
	@Override
	public void onDesBlock(Block b){}
	@Override
	public Item clickAt(double x,double y,Agent a){
		this.a=atan2(y-a.y,x-a.x)+rnd_gaussion()*0.5;
		return super.clickAt(x,y,a);
	}
	@Override
	public boolean onLongPress(Agent a,double x,double y){
		this.a=atan2(y-a.y,x-a.x)+rnd_gaussion()*0.5;
		return super.onLongPress(a,x,y);
	}
	@Override
	public void onCarried(Agent a){
		if(cd>0)--cd;
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(abs(h.x+h.dir*(h.width()+0.3)-a.x)<0.3&&abs(h.y-a.y)<0.3){
			if(cd<=0&&findEnergyCell(h,5)){
				h.attack();
				return true;
			}
		}else if(cd>0)return rnd()<0.5;
		return false;
	}
	@Override
	public void onUpdate(ThrowedItem ent){
		a=rnd(0,2*PI);
	}
	public void onBroken(double x,double y){
		//...
		super.onBroken(x,y);
	}
}

