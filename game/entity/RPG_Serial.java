package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.world.NearbyInfo;
import game.block.Block;
import util.BmpRes;
import game.item.Warhead;

public class RPG_Serial extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_Serial rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_Serial(game.item.RPG_Serial a){
		super();
		rpg=a;
		fuel*=2;
	}	@Override
	public void test_update(){
		super.test_update();
		f+=0.2*(xv*xv+yv*yv);
	}

	@Override
	public void update(){
		super.update();
		f+=0.2*(xv*xv+yv*yv);
		if(!rpg.wh1.isEmpty()){
			double xd=xv,yd=yv,d=3/sqrt(xv*xv+yv*yv+1e-8);
			xd*=d;yd*=d;
			double x1=x+xd,y1=y+yd;
			boolean flag=false;
			NearbyInfo ni=World.cur.getNearby(x1,y1,0.5,0.5,true,true,true);
			for(Block bs[]:ni.blocks){
				for(Block b:bs){
					if(!b.isCoverable())flag=true;
				}
			}
			for(Entity e:ni.ents){
				if(rnd()<e.RPG_ExplodeProb())flag=true;
			}
			for(Agent e:ni.agents){
				if(rnd()<e.RPG_ExplodeProb())flag=true;
			}
			if(flag)explode1();
		}
	}
	private void explode1(){
		Warhead wh1=rpg.wh1.popItem();
		if(wh1!=null){
			double xd=xv,yd=yv,d=1/sqrt(xv*xv+yv*yv+1e-8);
			xd*=d;yd*=d;
			Agent tmp=Agent.temp(x,y,0.3,0.3,xv>0?1:-1,this);
			tmp.xv=xv;tmp.yv=yv;
			wh1.onLaunchAtPos(tmp,xv>0?1:-1,x+xd,y+yd,yv/xv,0);
			/*wh1.explode(x+xd,y+yd,xv*2,yv*2,this,null);*/
		}
	}
	public void explode(){
		explode1();
		Warhead wh2=rpg.wh2.popItem();
		if(wh2!=null){
			wh2.explode(x,y,xv,yv,this,null);
		}
		super.explode();
	}
}
