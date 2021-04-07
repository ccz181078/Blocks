package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import game.block.Block;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class JumpBox extends Vehicle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp[]=BmpRes.load("Armor/JumpBox_",2);
	public BmpRes getBmp(){return bmp[0];}
	public BmpRes getArmorBmp(){
		return bmp[0];
	}
	
	SpecialItem<Block> block=new SpecialItem<>(Block.class);
	public ShowableItemContainer getItems(){
		return ItemList.create(ec,block);
	}
	
	public int maxDamage(){return 4000;}
	
	public double mass(){return 2;}
	public double width(){return 0.8;}
	public double height(){return 0.8;}

	double sx=0,sy=0;
	int cd=0;
	
	public double getJumpAcc(Human h,double v){return 0;}
	public void onUpdate(Human w){
		if(cd>0)--cd;
		w.f*=0.3;
		
		EnergyCell e=ec.get();
		if(!checkEnergy(50,w))return;
		
		block.insert(w.items.getSelected());
		
		if(cd<=0){
			sx+=w.xdir*0.03;
			sy+=w.ydir*0.03;
			loseEnergy((w.xdir*w.xdir+w.ydir*w.ydir)*10);
		}
		if(w.xdir==0&&w.ydir==0&&(sx!=0||sy!=0)){
			double s=hypot(sx,sy)+1e-8;
			double mv2=s/(1+s)/0.03*E0*10* 5;
			if(w.xdep!=0||w.ydep!=0||w.inblock>0){
				w.acc(sx,sy,mv2);
				double c=min(1,sx*sx+sy*sy);
				if(rnd()<c){
					++damage;
				}
			}else{
				cd=30;
				Block b=block.popItem();
				if(b!=null){
					if(sx==0)sx=1e-4;
					b.onLaunchAtPos(w,(sx>0?-1:1),w.x,w.y,sy/sx,mv2);
					++damage;
				}
			}
			sx=sy=0;
		}else{
			double c=0;
			
			if(w.ydep!=0)w.xf+=0.25;
			if(w.xdep!=0)w.yf+=0.25;
			
			if(w.xdep!=0){
				w.xa+=(w.xdep>0?1:-1)*0.02;
				c+=0.02;
			}
			if(w.ydep!=0){
				w.ya+=(w.ydep>0?1:-1)*0.02;
				c+=0.02;
			}
			
			if(w.xdep!=0||w.ydep!=0)w.anti_g+=1;
			
			if(rnd()<c){
				loseEnergy(1);
				++damage;
			}

		}
	}

	public Attack transform(Attack a){
		a=super.transform(a);
		if(a!=null){
			damage+=1.2*rf2i(a.val);
			a.val*=0.02;
		}
		return a;
	}
	public double onImpact(Human h,double v){
		v=max(0,v-5000)*0.05;
		damage+=rf2i(v);
		return v;
	}
	public void onBroken(double x,double y,Agent w){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		DroppedItem.dropItems(toArray(),x,y);
		Source src=SourceTool.item(w,this);
		if(rnd(0,1)<0.8){
			Spark.explode(x,y,0,0,60,0.1,3,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);
		}
		Fragment.gen(x,y,width(),height(),4,4,12,getBmp());
		super.onBroken(x,y,w);
	}
	
	@Override
	public void touchAgent(Human w,Agent a){
		a.onAttacked(w.v2rel(a)*100,SourceTool.item(w,this),this);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		getArmorBmp().draw(cv,0,0,(float)width(),(float)height());
		double s=hypot(sx,sy)+1e-8;
		double xa=-sx/(1+s),ya=-sy/(1+s);
		bmp[1].draw(cv,(float)xa,(float)ya,(float)width(),(float)height());
	}
}
