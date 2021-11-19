package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class FluidBox extends Vehicle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Armor/FluidBox");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){
		return bmp;
	}
	public double fluidResistance(){
		return 0.2;
	}

	protected float maxReload(){return 1.6f;}
	public int maxDamage(){return 5000;}
	
	public double mass(){return 4.5f;}
	public double width(){return 0.8;}
	public double height(){return 0.8;}
	@Override
	public boolean chkRigidBody(){
		return rnd()<0.05;
	}
	@Override
	public Attack transform(Attack a){
		a=super.transform(a);
		if(a!=null){
			a.val*=0.02;
		}
		return a;
	}
	@Override
	public void onUpdate(Human w){
		w.fc+=0.1/w.mass();
		
		if(!hasEnergy(1))return;
		
		double c=0;
		
		if(w.xdir!=0){
			w.xa+=w.xdir*0.003;
			c+=0.1;
		}
		if(w.ydir!=0){
			w.xa+=w.xdir*0.003;
			c+=0.1;
		}
		
		w.climbable=true;
		w.anti_g=1;
		
		if(c>0){
			if(rnd()<c){
				loseEnergy(1);
				if(rnd()<0.01)++damage;
			}
		}
	}
	
	
	public int maxAir(){
		return 20;
	}
	
	public double onImpact(Human h,double v){
		v=max(0,v-500)*0.1;
		damage+=rf2i(v);
		return v;
	}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		DroppedItem.dropItems(toArray(),x,y);
		Source src=SourceTool.item(this);
		if(rnd(0,1)<0.8){
			Spark.explode(x,y,0,0,60,0.1,3,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);
		}
		Fragment.gen(x,y,width(),height(),4,4,12,getBmp());
		super.onBroken(x,y);
	}
	
	@Override
	public void touchAgent(Human w,Agent a){
		a.onAttacked(w.v2rel(a)*100,SourceTool.item(w,this),this);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		getBmp().draw(cv,0,0,(float)width(),(float)height());
	}
}
