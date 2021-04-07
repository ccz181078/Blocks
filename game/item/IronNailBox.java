package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class IronNailBox extends Vehicle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/IronNailBox");
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/IronNailBox_",2);
	public BmpRes getBmp(){return bmp;}
	int cd=0;
	public BmpRes getArmorBmp(){
		if(cd>0)return bmp_armor[1];
		return bmp_armor[0];
	}
	public int maxDamage(){return 10000;}
	public double mass(){return 5;}
	public double width(){return 0.475;}
	public double height(){return 0.95;}
	
	public boolean onArmorLongPress(Human hu,double tx,double ty){
		cd=max(cd,90);
		return false;
	}
	
	public boolean onArmorClick(Human hu,double tx,double ty){
		cd=max(cd,60);
		return false;
	}

	public void onUpdate(Human w){
		if(cd>0)--cd;
		
		double f_=(cd>0?0.05:0.3);
		if(w.xdep!=0)w.yf+=f_;
		if(w.ydep!=0)w.xf+=f_;
		
		if(cd==0)
		for(Entity e:World.cur.getNearby(w.x,w.y,width()+2,height()+2,false,true,false).ents){
			if(e.harmless())continue;
			if(!hasEnergy(1))continue;
			loseEnergy(rf2i(0.1));
			double xd=abs(e.x-w.x)-width(),yd=abs(e.y-w.y)-height();
			double cx=xd<0?0:e.mass()*(abs(e.xv)*0.2+0.1)/(1+xd*2);
			double cy=yd<0?0:e.mass()*(abs(e.yv)*0.2+0.1)/(1+yd*2);
			int dx=e.x>w.x?1:-1,dy=e.y>w.y?1:-1;
			e.impulse(dx,0,cx);
			e.impulse(0,dy,cy);
			w.impulse(-dx,0,cx);
			w.impulse(0,-dy,cy);
		}
	}

	
	public Attack transform(Attack a){
		a=super.transform(a);
		if(a!=null){
			int s=rf2i(a.val);
			damage+=s;
			if(cd>0)a.val*=0.1;
			else{
				if(hasEnergy(1+s)){
					loseEnergy(1+s);
					return null;
				}
				a.val*=0.02;
			}
		}
		return a;
	}
	public double onImpact(Human h,double v){
		damage+=rf2i(v*0.2);
		if(cd>0)v*=0.1;
		else v=0;
		return v;
	}
	
	public void onBroken(double x,double y,Agent w){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		Source src=SourceTool.item(w,this);
		for(int i=0;i<80;++i){
			new game.entity.Bullet(new game.item.IronNail()).initPos(x-0.5,y+rnd(-1,1),-rnd(0.2,0.8),rnd_gaussion()*0.2,src).add();
			new game.entity.Bullet(new game.item.IronNail()).initPos(x+0.5,y+rnd(-1,1),+rnd(0.2,0.8),rnd_gaussion()*0.2,src).add();
		}
		for(int i=0;i<40;++i){
			new game.entity.Bullet(new game.item.IronNail()).initPos(x+rnd(-0.5,0.5),y-1,rnd_gaussion()*0.2,-rnd(0.2,0.8),src).add();
			new game.entity.Bullet(new game.item.IronNail()).initPos(x+rnd(-0.5,0.5),y+1,rnd_gaussion()*0.2,+rnd(0.2,0.8),src).add();
		}
		DroppedItem.dropItems(toArray(),x,y);
		Fragment.gen(x,y,width(),height(),4,4,12,getArmorBmp());
		super.onBroken(x,y,w);
	}
	@Override
	public void touchAgent(Human w,Agent a){
		a.onAttacked(w.v2rel(a)*300+3,SourceTool.item(w,this),this);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		if(cd>0){
			hu.armor.pop();
			super.draw(cv,hu);
			hu.armor.insert(this);
		}
		getArmorBmp().draw(cv,0,0,(float)width(),(float)height());
	}
}
