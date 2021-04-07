package game.entity;

import static util.MathUtil.*;
import game.item.SingleItem;
import graphics.Canvas;
import game.item.Item;
import game.item.ItemContainer;
import game.world.World;
import static java.lang.Math.*;
import game.block.Block;
import game.block.IronBoxBlock;
import game.block.WoodenBoxBlock;
import game.item.EnergyContainer;

public final class DroppedItem extends Entity{
	private static final long serialVersionUID=1844677L;
	public SingleItem item=new SingleItem();
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	public DroppedItem(double _x,double _y,SingleItem _item){
		x=_x+rnd(-0.01,0.01);
		y=_y+rnd(-0.01,0.01);
		xv=rnd(-0.05,0.05);
		yv=rnd(-0.05,0.05);
		item.insert(_item);
		hp=10+rnd_exp(5);
	}
	
	
	@Override
	public double buoyancyForce(){return 1.5;}
	@Override
	public double fluidResistance(){return 0.2;}
	
	public double mass(){return 0.1;}
	@Override
	public double RPG_ExplodeProb(){return 0;}
	
	@Override
	public boolean harmless(){return true;}
	@Override
	public boolean chkEnt(){return false;}//是否与Entity接触
	
	public static void dropItems(SingleItem si[],double _x,double _y){
		if(si!=null)
		for(SingleItem s:si){
			if(!s.isEmpty()){
				new DroppedItem(_x,_y,s).add();
			}
		}
	}
	public static void dropItems(ItemContainer ic,double _x,double _y){
		if(ic!=null)dropItems(ic.toArray(),_x,_y);
	}
	public double width(){return 0.3f;}
	public double height(){return 0.3f;}
	public DroppedItem(int _x,int _y,SingleItem _item){
		this(_x+rnd(0.3,0.7),_y+rnd(0.3,0.7),_item);
	}
	public void update(){
		if(item.isEmpty()){
			remove();
			return;
		}
		if(in_wall){
			double x1=rnd_gaussion(),y1=rnd_gaussion();
			if(!World.cur.get(x+x1,y+y1).isSolid()){
				double d=0.01/(abs(x1)+abs(y1)+0.01);
				new SetRelPos(this,this,x1*d,y1*d);
			}
		}
		int xD=2,yD=1,x1=rndi(-xD,xD),y1=rndi(-yD,yD);
		if(x1!=0||y1<=0){
			x1+=f2i(x);
			y1+=f2i(y);
			Block b=World.cur.get(x1,y1).rootBlock();
			if(b.getClass()==game.block.ItemAbsorberBlock.class){
				EnergyContainer e=(EnergyContainer)(game.block.ItemAbsorberBlock)b;
				if(e.hasEnergy(1)){
					e.loseEnergy(1);
					xv=yv=0;
					new SetRelPos(this,null,f2i(x1)+rnd(0.31,0.69),f2i(y1)+1.31);
				}
			}
		}
		if(rnd()<0.03){
			for(Entity e:game.world.World.cur.getNearby(x,y,width(),height(),false,true,false).ents){
				if(!(e instanceof DroppedItem))continue;
				if(e==this)continue;
				item.insert(((DroppedItem)e).item);
				hp=max(hp,e.hp);
			}
		}
		super.update();
		hp-=0.003f;
	}
	public static interface Picker{
		void pick(DroppedItem item);
	}
	public void touchAgent(Agent a){
		if(a instanceof Picker){
			((Picker)a).pick(this);
		}
	}
	
	@Override
	public void onKill(){
		Item i=item.popItem();
		if(i!=null)i.onVanish(x,y,getSrc());
		if(!item.isEmpty())new DroppedItem(x,y,item).setHpScale(0.1).add();
	}

	void touchBlock(int x,int y,Block b){
		if(b.getClass()==IronBoxBlock.class&&rnd()<0.2){
			((IronBoxBlock)b).insert(item);
			if(item.isEmpty())remove();
			return;
		}
		if(b.getClass()==WoodenBoxBlock.class&&rnd()<0.2){
			((WoodenBoxBlock)b).insert(item);
			if(item.isEmpty())remove();
			return;
		}
		super.touchBlock(x,y,b);
	}
	
	public void draw(Canvas cv){
		Item it=item.get();
		if(it!=null)it.getBmp().draw(cv,0,0,(float)width(),(float)height());
	}
};
