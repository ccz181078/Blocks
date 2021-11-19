package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class FastBox extends Vehicle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp[]=BmpRes.load("Armor/FastBox_",6);
	public BmpRes getBmp(){return bmp[0];}
	public BmpRes getArmorBmp(){
		if(damage<1500)return bmp[0+flag*3];
		if(damage<3000)return bmp[1+flag*3];
		return bmp[2+flag*3];
	}
	
	public int maxDamage(){return 4000;}
	
	public double mass(){return 3;}
	public double width(){return 0.8;}
	public double height(){return 0.8;}
	int flag = 0;
	public boolean onArmorClick(Human a,double tx,double ty){
		if( abs(tx-a.x) < width() && abs(ty-a.y) < height() )
			if( flag == 0 && reload > 0.4 )
				flag = 1;
		a.dir=tx>a.x?1:-1;
		return false;
	}
	public boolean onArmorLongPress(Human a,double tx,double ty){
		a.dir=tx>a.x?1:-1;
		return false;
	}
	
	public void onUpdate(Human w){
		EnergyCell e=ec.get();
		if( flag != 0 ) reload -= 0.01;
		else reload += 0.00016 * ( ( maxReload() - reload ) / maxReload() * 5.4 + 1 );
		if(reload>maxReload())reload=maxReload();
		if(reload<0)reload=0;
		if(!checkEnergy(5,w))return;
		double c=0;
		
		int xdir=w.xdir;
		if(xdir==0&&w.xdep!=0)xdir=(w.xdep>0?1:-1);
		
		if(w.ydep!=0||w.xdep!=0)w.xa+=xdir*0.04;
		else w.xa+=xdir*0.01;
		if(xdir!=0)c+=0.04;
		
		
		int ydir=w.ydir;
		if(ydir==0&&w.ydep!=0)ydir=(w.ydep>0?1:-1);
		
		if(w.xdep!=0&&w.ydep>=0&&ydir==0)w.ya+=w.gA();
		else if(w.ydep!=0||w.xdep!=0||flag==1)w.ya+=ydir*0.04;
		else w.ya+=ydir*0.01;
		if(ydir!=0)c+=0.04;
		
		if(c>0){
			if(rnd()<c){
				loseEnergy(2);
				if(rnd()<0.01)++damage;
			}
		}
		if( reload < 0.01 ) flag = 0;
	}

	public Attack transform(Attack a){
		a=super.transform(a);
		if(a!=null){
			damage+=rf2i(1.2*a.val);
			a.val*=0.02;
		}
		return a;
	}
	public double onImpact(Human h,double v){
		v=max(0,v-100)*0.05;
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
		getArmorBmp().draw(cv,0,0,(float)width(),(float)height());
	}
}
