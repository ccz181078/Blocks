package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.entity.Entity.*;
import game.item.*;
import game.ui.*;
import game.block.BlockAt;
import game.world.World;
import graphics.Canvas;
import util.BmpRes;
import game.block.*;

public class Zombie extends Human{
	private static final long serialVersionUID=1844677L;

	static BmpRes
	body=new BmpRes("Entity/Zombie/body"),
	hand=new BmpRes("Entity/Zombie/hand"),
	leg=new BmpRes("Entity/Zombie/leg");

	BmpRes bodyBmp(){return body;}
	BmpRes handBmp(){return hand;}
	BmpRes legBmp(){return leg;}
	
	Goal goal=new KillPlayer();
	
	public Zombie(double _x,double _y){
		super(_x,_y);
	}

	public void action(){
		goal=goal.update();
		super.action();
	}

	public void onAttacked(game.entity.Attack a){
		super.onAttacked(a);
		if(a.src!=null){
			attack();
		}
	}

	public boolean chkRemove(long t){return t>30*300;}
	
	void onKill(){
		dropItems();
		super.onKill();
	}

abstract class Goal implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	Goal prev;
	abstract public Goal update();
}

class KillPlayer extends Goal{
	private static final long serialVersionUID=1844677L;
	Player pl=null;
	private final void findPlayer(){
		double md=60;
		for(Player p:World.cur.getPlayers()){
			double d=abs(x-p.x)+abs(y-p.y)*2;
			if(d<md){
				d=md;
				pl=p;
				return;
			}
		}
	}
	public Goal update(){
		if(pl==null){
			if(rnd()<0.05)findPlayer();
			return this;
		}
		if(pl.removed){
			pl=null;
			return this;
		}
		if(hp<maxHp()*0.8&&rnd()<0.2){
			EatFood g=new EatFood();
			g.prev=this;
			return g;
		}
		if(rnd()<0.5){
			MoveToAgent g=new MoveToAgent();
			g.prev=this;
			g.a=pl;
			return g;
		}
		Attack g=new Attack();
		g.prev=this;
		g.a=pl;
		return g;
	}
}

class EatFood extends Goal{
	private static final long serialVersionUID=1844677L;
	public Goal update(){
		int mv=0;
		for(SingleItem s:items.toArray())if(!s.isEmpty()){
			Item it=s.get();
			int v=it.foodVal();
			if(v>mv&&v<(maxHp()-hp)*1.5){
				mv=v;
				items.select(s);
			}
		}
		if(mv>0)eat(items.popItem());
		return prev;
	}
}

class Attack extends Goal{
	private static final long serialVersionUID=1844677L;
	Agent a;
	public Goal update(){
		final double w1=0.3f,h1=0.5f,x1=x+dir*(width()+w1);
		if(abs(a.x-x1)<w1+a.width()&&abs(a.y-y)<h1+a.height()){
			attack();
			PhysicsAttack g=new PhysicsAttack();
			g.prev=prev;
			return g;
		}
		double xd=abs(a.x-x);
		if(xd>width()&&xd<4&&abs(a.y-y)<xd){
			RangedAttack g=new RangedAttack();
			g.prev=prev;
			g.a=a;
			return g;
		}
		return prev;
	}
}


class RangedAttack extends Goal{
	private static final long serialVersionUID=1844677L;
	Agent a;
	public Goal update(){
		SingleItem[] si=items.toArray();
		for(int t=0;t<5;++t){
			int x=rndi(0,si.length-1);
			if(!si[x].isEmpty()){
				items.select(si[x]);
				clickAt(a.x+rnd(-a.width(),a.width()),a.y+rnd(-a.height(),a.height()));
				return prev;
			}
		}
		return prev;
	}
}

class PhysicsAttack extends Goal{
	private static final long serialVersionUID=1844677L;
	public Goal update(){
		int mv=0;
		for(SingleItem s:items.toArray())if(!s.isEmpty()){
			Item it=s.get();
			int v=it.swordVal();
			if(v>mv){
				mv=v;
				items.select(s);
			}
		}
		return prev;
	}
}

class DesBlock extends Goal{
	private static final long serialVersionUID=1844677L;
	BlockAt ba;
	public DesBlock(Goal _prev,BlockAt _ba){prev=_prev;ba=_ba;}
	public Goal update(){
		setDes(ba.x,ba.y);
		int mv=0;
		if(ba.exist()){
			if(World.cur.destroyable(ba.x,ba.y))
			for(SingleItem s:items.toArray())if(!s.isEmpty()){
				Item it=s.get();
				int v=0;
				if(ba.block instanceof DirtType)v=it.shovelVal();
				if(ba.block instanceof StoneType)v=it.pickaxVal();
				if(ba.block instanceof WoodenType)v=it.axVal();
				if(v>mv){
					mv=v;
					items.select(s);
				}
			}
		}
		return prev;
	}
}

class MoveToAgent extends Goal{
	private static final long serialVersionUID=1844677L;
	Agent a;
	public Goal update(){
		if(a.removed){
			xdir=ydir=0;
			return prev;
		}
		
		if(abs(x-a.x)<width()+a.width()&&(a.x-x)*xdir>0)xdir=0;
		else if(x<a.x)xdir=1;
		else xdir=-1;
		
		if(abs(x-a.x)>6||abs(y-a.y)<height()+a.height())ydir=0;
		else if(y<a.y)ydir=1;
		else ydir=-1;
		
		if(rnd()<0.02)cancelDes();
		
		if(in_wall&&rnd()<0.5){
			BlockAt ba=World.cur.get1(f2i(x+rnd(-width(),width())),f2i(y+rnd(-height(),height())));
			if(ba.block.isSolid())return new DesBlock(this,ba);
		}
		
		if(xdir!=0&&xdep!=0&&rnd()<0.5){
			if(rnd()<0.3)xdir=ydir=0;
			else ydir=1;
			BlockAt ba=World.cur.get1(f2i(x+(width()+0.1)*xdir*rnd()),f2i(y+height()*rnd(0.2,1.1)));
			if(ba.block.isSolid())return new DesBlock(this,ba);
		}
		
		if(ydep>0&&rnd()<0.7){
			BlockAt ba=World.cur.get1(f2i(x+rnd(-width(),width())),f2i(y+height()*rnd(0.5,1.1)));
			if(ba.block.isSolid())return new DesBlock(this,ba);
		}
		
		if(ydir<0&&ydep<0&&rnd()<0.5){
			BlockAt ba=World.cur.get1(f2i(x+rnd(-width(),width())),f2i(y-height()));
			if(ba.block.isSolid())return new DesBlock(this,ba);
		}
		
		return prev;
	}
}
};
