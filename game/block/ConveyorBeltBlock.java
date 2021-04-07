package game.block;

import util.BmpRes;
import game.entity.Entity;
import game.entity.Agent;
import game.entity.Human;
import static java.lang.Math.*;
import graphics.Canvas;
import game.world.World;
import game.world.NearbyInfo;

public class ConveyorBeltBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp[]=BmpRes.load("Block/ConveyorBeltBlock_",8);
	public BmpRes getBmp(){
		int t=(int)(game.world.World.cur.time%4);
		return bmp[tp/2*4+((tp&1)==1?t:3-t)];
	}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public boolean forceCheckEnt(){return true;}
	public boolean chkNonRigidEnt(){return true;}
	static double xv[]={-1/16.,1/16.,0,0},yv[]={0,0,-1/16.,1/16.};
	public boolean isSolid(){return false;}
	public double transparency(){return 0;}
	int maxDamage(){return 50;}
	private int tp=0;
	public void touchEnt(int x,int y,Entity ent){
		super.touchEnt(x,y,ent);
		double k=intersection(x,y,ent);
		double xa=(xv[tp]-ent.xv)*k*0.2;
		double ya=(yv[tp]-ent.yv)*k*0.2;
		ent.xa+=xa;
		ent.ya+=ya;
		ent.climbable=true;
		/*ent.f+=(abs(xa)+abs(ya))*10;
		ent.inblock+=k;
		ent.anti_g+=k*4;*/
	}
	
	double frictionIn1(){return 0;}
	double frictionIn2(){return 0;}
	
	@Override
	public void onFall(game.entity.FallingBlock b){
		check(b.x,b.y,0.5,0.5,xv[tp]+b.xv,yv[tp]+b.yv,b);
	}
	
	public static void check(double x,double y,double xd,double yd,double xv,double yv,Entity e0){
		NearbyInfo ni=World.cur.getNearby(x,y,xd,yd,false,true,true);
		for(Entity e:ni.ents){
			if(e==e0)continue;
			double xc=min(x+xd,e.right)-max(x-xd,e.left);
			double yc=min(y+yd,e.top)-max(y-yd,e.bottom);
			double k=max(0,xc*yc/e.V)*0.5;
			e.xa+=(xv-e.xv)*k;
			e.ya+=(yv-e.yv)*k;
		}
		for(Entity e:ni.agents){
			if(e==e0)continue;
			double xc=min(x+xd,e.right)-max(x-xd,e.left);
			double yc=min(y+yd,e.top)-max(y-yd,e.bottom);
			double k=max(0,xc*yc/e.V)*0.2;
			e.xa+=(xv-e.xv)*k;
			e.ya+=(yv-e.yv)*k;
		}
	}
	public static void draw(Canvas cv,double x,double y,double xd,double yd,double angle){
		
	}
	
	@Override
	public void onCarried(game.entity.Agent a){
		check(a.x+a.dir*(a.width()+0.3),a.y,0.3,0.3,xv[tp],yv[tp],a);
	}

	public BmpRes getUseBmp(){return rotate_btn;}
	public void onUse(Human a){
		tp=(tp+1)%4;
		super.onUse(a);
	}
}
