package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.block.Block;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class CEBox extends Vehicle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/CEBox_",3);
	public BmpRes getBmp(){return getArmorBmp();}
	int state=0,fb_dir=0;
	public BmpRes getArmorBmp(){
		return bmp_armor[state];
	}
	public int maxDamage(){return 10000;}
	public double mass(){return 5;}
	public double width(){return 0.475;}
	public double height(){return 0.475;}
	
	SpecialItem<Block> block=new SpecialItem<>(Block.class);
	game.entity.FallingBlock fb=null;
	
	public ShowableItemContainer getItems(){
		return ItemList.create(ec,block);
	}
	
	public boolean onArmorLongPress(Human hu,double tx,double ty){
		des(f2i(tx),f2i(ty),hu);
		return false;
	}
	
	public boolean onArmorClick(Human hu,double tx,double ty){
		if(abs(tx-hu.x)<width()&&abs(ty-hu.y)<height()){
			state=(state+1)%3;
		}
		return false;
	}
	
	private void des(int x,int y,Human w){
		if(!hasEnergy(1))return;
		if(!w.destroyable(x,y))return;
		World.cur.get(x,y).des(x,y,32,this);
		loseEnergy(1);
		if(rnd()<0.1)++damage;
	}
	
	private void fall(int x,int y,int dir,Human w){
		if(!hasEnergy(1))return;
		Block b=World.cur.get(x,y);
		if(!b.fallable()){
			if(b.isSolid())des(x,y,w);
		}else if(!b.isCoverable()){
			World.cur.setVoid(x,y);
			fb=new FallingBlock(x,y,b);
			fb_dir=-dir;
			fb.add();
		}
	}
	
	private void place(int x,int y,Human w){
		if(!hasEnergy(1))return;
		Block b=World.cur.get(x,y);
		if(b.isCoverable()){
			b=block.popItem();
			if(b!=null){
				b.src=w;
				World.cur.place(x,y,b);
				loseEnergy(1);
				if(rnd()<0.1)++damage;
			}
		}
	}
	
	@Override
	public double getJumpAcc(Human h,double v){
		if(fb!=null)return 0;
		return v;
	}

	public void onUpdate(Human w){
		if(fb!=null&&(w.in_wall||fb.isRemoved()||abs(w.x-fb.x)>1||abs(w.y-fb.y)>1))fb=null;
		
		if(fb!=null&&hasEnergy(1)){
			fb.xa+=0.1*fb_dir;
			fb.ya+=0.05;
			fb.xf+=0.3;
			fb.hp=20;
			if(w.xdir!=-fb_dir)w.xf+=0.5;
			else{
				w.xa-=0.1*fb_dir;
				w.ya-=0.08;
				w.xf+=0.3;
			}
			loseEnergy(1);
		}
		
		if(!hasEnergy(1))state=0;
		
		int x=f2i(w.x),y=f2i(w.y-height()+0.1);
		
		if(state==2){
			double xa=w.xdir*0.06,ya=w.ydir*0.06;
			if(w.xdir!=0||w.ydir!=0){
				loseEnergy(1);
				if(rnd()<0.1)++damage;
			}
			new SetRelPos(w,w,xa,ya);
		}
		
		if(state==1){
			block.insert(w.items.getSelected());
			if(!w.in_wall&&w.ydep<0){
				int y1=f2i(w.y-height()-0.1);
				place(x,y1,w);
				if(w.xdep<0)fall(x-1,y,-1,w);
				if(w.xdep>0)fall(x+1,y,1,w);
			}
			double xa=0,ya=0;
			if(w.ydir>0){
				if(!World.cur.get(x,y-1).isCoverable()){
					Block b=World.cur.get(x,y);
					if(b.isCoverable()){
						place(x,y,w);
					}else{
						xa+=(x+0.5-w.x)*0.1;
						ya+=0.3;
					}
				}
			}
			if(w.ydir<0){
				Block b=World.cur.get(x,y);
				if(!b.isCoverable()){
					des(x,y,w);
					xa+=(x+0.5-w.x)*0.1;
				}else{
					xa+=(x+0.5-w.x)*0.1;
					ya-=0.3;
				}
			}
			if(abs(xa)>0.01)ya=0;
			else if(World.cur.get(x,f2i(w.y+height()+ya)).isSolid())ya=0;
			new SetRelPos(w,w,xa,ya);
		}
		
		if(state==0){
			Block b=World.cur.get(x,y);
			if(b.isSolid())des(x,y,w);
		}
		
	}

	
	public Attack transform(Attack a){
		a=super.transform(a);
		if(a!=null){
			if(a instanceof NormalAttack){
				a.val=max(0,a.val*0.04-0.5);
				damage+=rf2i(a.val);
			}else{
				damage+=rf2i(a.val);
				a.val*=0.04;
			}
		}
		return a;
	}
	public double onImpact(Human h,double v){
		v=max(0,v-20);
		damage+=rf2i(v*0.1);
		return v*0.005;
	}
	
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		DroppedItem.dropItems(toArray(),x,y);
		Fragment.gen(x,y,width(),height(),4,4,12,getArmorBmp());
		super.onBroken(x,y);
	}
	
	@Override
	public void touchAgent(Human w,Agent a){
		double c=4;
		if(hasEnergy(1)){
			loseEnergy(1);
			if(rnd()<0.1)++damage;
			c=32;
		}
		a.onAttacked(w.v2rel(a)*20+c,SourceTool.item(w,this),this);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		getArmorBmp().draw(cv,0,0,(float)width(),(float)height());
	}
}
