package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.*;
import game.block.Block;
import util.BmpRes;

public class RPG_IronNail_Directed extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_IronNail_Directed rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_IronNail_Directed(game.item.RPG_IronNail_Directed a){
		super();
		rpg=a;
	}
	@Override
	public void update(){
		super.update();
		double xd=xv,yd=yv,d=3/sqrt(xv*xv+yv*yv+1e-8);
		xd*=d;yd*=d;
		double x1=x+xd,y1=y+yd;
		boolean flag=false;
		NearbyInfo ni=World.cur.getNearby(x1,y1,0.5,0.5,true,false,true);
		for(Block bs[]:ni.blocks){
			for(Block b:bs){
				if(!b.isCoverable())flag=true;
			}
		}
		for(Agent e:ni.agents){
			if(rnd()<e.RPG_ExplodeProb())flag=true;
		}
		if(flag)try_explode();
	}
	@Override
	public void explode(){
		double v=hypot(xv,yv)+0.3;
		explodeDirected(this,40,0.1,v*1.5,v*0.5,1);
		super.explode();
	}
	@Override
	public Entity getBall(){return new Bullet(new game.item.IronNail());}
}
