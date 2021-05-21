package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class ExplosiveBox extends Vehicle{
	private static BmpRes bmp=new BmpRes("Armor/ExplosiveBox");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){
		return bmp;
	}
	public SingleItem ex=new SingleItem();
	public ShowableItemContainer getItems(){return ItemList.create(ec,ex);}
	
	public int maxDamage(){return 3000;}
	
	public double mass(){return 2;}
	public double width(){return 0.6;}
	public double height(){return 0.6;}
	
	public boolean onArmorClick(Human a,double tx,double ty){
		if( abs(tx-a.x) < width() && abs(ty-a.y) < height() ) {
			damage=maxDamage();
			Source src=SourceTool.item(this);
			Item it=ex.get();
			if(it!=null)it.onExplode(a,tx,ty,ex.getAmount(),src);
			ex.clear();
			a.armor.popItem();
			a.teleporting=true;
			new TeleportationEvent(a,new TeleportationSquare());
			return true;
		}
		return false;
	}
	public boolean onArmorLongPress(Human a,double tx,double ty){
		a.dir=tx>a.x?1:-1;
		return false;
	}
	
	public void onUpdate(Human w){
		EnergyCell e=ec.get();
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
		else if(w.ydep!=0||w.xdep!=0)w.ya+=ydir*0.04;
		else w.ya+=ydir*0.01;
		if(ydir!=0)c+=0.04;
		
		if(c>0){
			if(rnd()<c){
				loseEnergy(2);
				if(rnd()<0.01)++damage;
			}
		}
	}

	public Attack transform(Attack a){
		a=super.transform(a);
		if(a!=null){
			damage+=rf2i(a.val);
			a.val*=0.02;
		}
		return a;
	}
	public double onImpact(Human h,double v){
		v=max(0,v-100)*0.2;
		damage+=rf2i(v);
		return v;
	}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		DroppedItem.dropItems(toArray(),x,y);
		Fragment.gen(x,y,width(),height(),4,4,12,getBmp());
		super.onBroken(x,y);
	}
	
	@Override
	public void touchAgent(Human w,Agent a){
		//a.onAttacked(w.v2rel(a)*100,SourceTool.item(w,this),this);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		getArmorBmp().draw(cv,0,0,(float)width(),(float)height());
	}
}
