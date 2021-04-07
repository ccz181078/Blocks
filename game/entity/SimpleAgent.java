package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.item.Item;
import game.block.BlockAt;
import graphics.Canvas;
import game.world.Weather;
import game.block.AirBlock;
import util.BmpRes;
import game.GameSetting;
import game.GlobalSetting;

public abstract class SimpleAgent extends Agent{
	private static final long serialVersionUID=1844677L;
	
	Agent target;
	
	SimpleAgent(double _x,double _y){super(_x,_y);}
	
	@Override
	public void ai(){
		if(target!=null&&target.isRemoved())target=null;
		if(ydep<0&&ydir<0)ydir=0;
		if(target!=null&&rnd()<0.1){
			xdir=x<target.x?1:-1;
			ydir=y<target.y?1:-1;
			if(rnd()<targetNullProb())target=null;
		}
		if(rnd()<(xdir==0?0.05:0.01)){
			xdir=rndi(-1,1);
			ydir=rndi(-1,1);
		}
		
		if(in_wall){
			if(y<120)ydir=1;
			else ydir=-1;
		}
		
		if(xdir!=0
			&& ydep!=0
			&&!World.cur.get(x+3*xdir,y-1).isSolid()
			&&!World.cur.get(x+3*xdir,y-3).isSolid()
		){
			xdir*=-1;
		}
		
		addHp(0.001);
	}
	public void onAttacked(Attack a){
		super.onAttacked(a);
		if(a.src!=null)target=a.src.getSrc();
	}
	protected double targetNullProb(){return 0.03;}

}

