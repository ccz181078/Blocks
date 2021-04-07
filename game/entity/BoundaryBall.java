package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.*;
import game.block.Block;

public class BoundaryBall extends IronBall{
private static final long serialVersionUID=1844677L;
	NonOverlapSpecialItem<Warhead> warhead=new NonOverlapSpecialItem<Warhead>(Warhead.class);
	public BoundaryBall(SpecialItem<Warhead> warhead){
		super();
		hp=300;
		if(!Entity.is_test)this.warhead.insert(warhead);
	}
	
	boolean exploded=false;
	void explode(Entity a){
		if(exploded||warhead.isEmpty())return;
		exploded=true;
		Warhead w=warhead.popItem();
		w.explode(x,y,xv,yv,this,null);
		kill();
	}
	
	@Override
	void touchAgent(Agent ent){
		if(rnd()<ent.RPG_ExplodeProb())explode(ent);
		super.touchAgent(ent);
	}

	
	int touched=0;
	public void update(){
		super.update();
		if(xdep!=0||ydep!=0||in_wall)hp-=0.1;
		if(xdep!=0||ydep!=0)touched=2;
		if(xdep!=0)xa+=(xdep>0?1:-1)*0.04;
		else if(ydep!=0)ya+=(ydep>0?1:-1)*0.01;
		hp-=0.03;
		if(abs(av)>0.01&&abs(av)<0.2)aa+=(av>0?1:-1)*0.03;
	}
	public void update0(){
		super.update0();
		touched=max(0,touched-1);
	}
	public double gA(){return touched!=0?0:super.gA();}
	void onKill(){
		new game.item.Iron().drop(x,y,rndi(4,6));
		DroppedItem.dropItems(warhead,x,y);
	}
	public BmpRes getBmp(){return game.item.BoundaryBall.bmp;}
}
