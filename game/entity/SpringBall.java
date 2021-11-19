package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.*;
import game.block.Block;

public class SpringBall extends IronBall{
	NonOverlapSpecialItem<Warhead> warhead=new NonOverlapSpecialItem<Warhead>(Warhead.class);
	public SpringBall(SpecialItem<Warhead> warhead){
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

	protected double K(){return 0.9;}
	void onKill(){
		new game.item.Iron().drop(x,y,rndi(1,2));
		DroppedItem.dropItems(warhead,x,y);
	}
	public BmpRes getBmp(){return game.item.SpringBall.bmp;}
}
