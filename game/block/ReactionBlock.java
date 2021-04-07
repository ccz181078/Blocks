package game.block;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.Entity;
import game.world.World;
import game.entity.Agent;
import game.entity.Zombie;

public class ReactionBlock extends Block{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/ReactionBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 10000;}
	public boolean circuitCanBePlaced(){return false;}
	public boolean isSolid(){return false;}
	public boolean isCoverable(){return false;}
	public void des(int x,int y,int v){}

	@Override
	public double light(){return 1;}

	@Override
	public double transparency(){return 0;}
	
	

	@Override
	public boolean onUpdate(int x,int y){
		if(rnd()<0.02){
			int x1=x,y1=y,d=rnd()<0.5?-1:1;
			if(rnd()<0.5)x1+=d;
			else y1+=d;
			if(World.cur.get(x1,y1).isCoverable()){
				World.cur.setAir(x,y);
				return true;
			}
		}
		return super.onUpdate(x,y);
	}

	public boolean forceCheckEnt(){return true;}
	public boolean chkNonRigidEnt(){return true;}
	@Override
	public void touchEnt(int x,int y,Entity ent){
		if(ent instanceof Agent){
			//
		}else{
			ent.hp-=3;
			double x_=0,y_=0;
			double xd=ent.x-(x+0.5);
			double yd=ent.y-(y+0.5);
			if(ent.yv*yd<0&&abs(xd)<0.5)y_=-2*ent.yv;
			if(ent.xv*xd<0&&abs(yd)<0.5)x_=-2*ent.xv;
			ent.impulse(x_,y_,ent.mass());
			double k=intersection(x,y,ent);
			ent.f+=k*0.1;
		}
		super.touchEnt(x,y,ent);
	}
	
}
