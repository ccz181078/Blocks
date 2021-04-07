package game.entity;

import static util.MathUtil.*;
import game.item.SingleItem;
import graphics.Canvas;
import game.item.Item;
import game.item.Armor;
import game.item.ItemContainer;
import game.world.World;
import static java.lang.Math.*;
import game.block.Block;
import game.block.IronBoxBlock;

public final class ThrowedItem extends Entity{
	private static final long serialVersionUID=1844677L;
	Item item;
	boolean attacking=true,des_flag=true,no_kill=false;
	public double hardness(){return item.hardness();}
	public ThrowedItem(double _x,double _y,Item _item){
		x=_x;
		y=_y;
		item=_item;
		hp=10;
	}
	
	@Override
	public String getName(){
		String s=item.getName();
		return s;
	}
	public double width(){return 0.3f;}
	public double height(){return 0.3f;}
	public double mass(){return 0.2;}
	
	@Override
	public void update(){
		super.update();
		item.onUpdate(this);
		if(item.isBroken())kill();
	}
	
	@Override
	public void touchAgent(Agent a){
		super.touchAgent(a);
		if(!attacking)return;
		a.onAttacked(item.swordVal()*(0.3+v2rel(a)*3),this);
		item.onAttack(a,this);
		if(a instanceof Human){
			Human h=(Human)a;
			Armor ar=h.armor.get();
			if(!(h.des_flag||h.attack_flag)&&rnd()<0.05&&!(ar!=null&&ar.isClosed()))h.throwCarriedItem(false,0);
		}
		hp-=1;
	}
	@Override
	public void touchEnt(Entity a){
		super.touchEnt(a);
		if(!attacking)return;
		if(a.harmless())return;
		a.onAttacked(item.swordVal()*(0.3+v2rel(a)*3),this);
		item.onAttack(a,this);
		hp-=1;
	}

	@Override
	void touchBlock(int x,int y,Block b){
		super.touchBlock(x,y,b);
		if(!b.isCoverable()){
			hp-=1;
			if(b.isSolid())hp-=2;
		}
		if(!des_flag)return;
		b.onPress(x,y,item);
	}

	@Override
	public void draw(Canvas cv){
		cv.rotate(World.cur.time*20%360);
		item.getBmp().draw(cv,0,0,(float)width(),(float)height());
	}

	@Override
	void onKill(){
		if(no_kill)return;
		super.onKill();
		if(item.isBroken())item.onBroken(x,y);
		else item.drop(x,y);
	}
	
	
};
