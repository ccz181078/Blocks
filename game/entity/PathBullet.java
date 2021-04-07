package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class PathBullet extends Bullet{
	private static final long serialVersionUID=1844677L;
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return 0.15;}
	public boolean chkRigidBody(){return false;}
	public boolean rotByVelDir(){return true;}
	boolean exploded=false;
	long tick;
	
	int state=0;
	
	public PathBullet(game.item.PathBullet b){
		super(b);
		hp=200;
		tick=World.cur.time;
	}
	Agent target=null;
	private void updateTarget(Agent w){
		if(w==null)return;
		if(target==null)target=w;
		else if(distL2(w)<distL2(target))target=w;
	}
	@Override
	void touchEnt(Entity ent){
		if((ent instanceof PathBullet))return;
		super.touchEnt(ent);
	}
	double near_cnt=1;
	public void update(){
		super.update();
		fc+=0.01/mass();
		game.world.NearbyInfo ni=World.cur.getNearby(x,y,4,4,false,true,true);
		double xvs=xv,yvs=yv,s=1;
		double xds=xv+rnd_gaussion()*0.1,yds=yv+rnd_gaussion()*0.1;
		PathBullet nxt=null,prv=null;
		for(Entity e:ni.ents){
			double xd=x-e.x,yd=y-e.y,d=1/(hypot(xd,yd)+1e-2);
			if(d<1/4.)continue;
			if(e instanceof PathBullet){
				PathBullet pb=((PathBullet)e);
				if(pb.tick<tick){
					if(nxt==null||nxt.tick<pb.tick)nxt=pb;
				}
				if(pb.tick>tick){
					if(prv==null||prv.tick>pb.tick)prv=pb;
				}
			}else{
				double c=d*d;
				xds+=xd*c;
				yds+=yd*c;
			}
		}
		if(nxt!=null)updateTarget(nxt.target);
		if(prv!=null)updateTarget(prv.target);
		if(target!=null){
			state=0;//find target 
			Agent src=target;
			double xd=(src.x+src.xv*2)-(x+xv*2),yd=(src.y+src.yv*2)-(y+yv*2),d=hypot(xd,yd)+1e-8;
			double c=0.1/(1+sqrt(d));
			xa+=c*xd/d;
			ya+=c*yd/d;
		}
		for(int i=0;i<3;++i){
			double xd=rnd_gaussion()*2,yd=rnd_gaussion()*2;
			if(World.cur.get(x+xd,y+yd).isSolid()){
				xa-=xd*0.01;
				ya-=yd*0.01;
			}
		}
		if(nxt==null){
			if(target!=null){
			}else{
				state=1;//look for target
				/*Agent src=source.getSrc();
				double xd=(src.x+src.xv*2)-(x+xv*2),yd=(src.y+src.yv*2)-(y+yv*2),d=hypot(xd,yd)+1e-8;
				double c=-1*0.01;
				xa+=c*xd/d;
				ya+=c*yd/d;*/
			}
		}else if(prv==null){
			state=3;//wait for more
			/*Agent src=source.getSrc();
			double xd=(src.x+src.xv*2)-(x+xv*2),yd=(src.y+src.yv*2)-(y+yv*2),d=hypot(xd,yd)+1e-8;
			if(d<10){
				double c=max(-2,min(2,d-3))*0.03;
				xa+=c*xd/d;
				ya+=c*yd/d;
			}else{*/
			fc+=0.1/mass();
			//}
		}else{
			state=2;//inner node
			double xd=((nxt.x+nxt.xv*2)+(prv.x+prv.xv*2))/2-(x+xv*2),yd=((nxt.y+nxt.yv*2)+(prv.y+prv.yv*2))/2-(y+yv*2),d=hypot(xd,yd)+1e-8;
			double c=min(2,d*2)*0.03;
			xa+=c*xd/d;
			ya+=c*yd/d;
		}
		for(Agent a:ni.agents){
			Agent b=a;
			if(a instanceof Airship_Flank){
				Airship_Flank af=(Airship_Flank)a;
				if(af.airship!=null)b=af.airship;
			}
			if(b==source.getSrc())continue;
			updateTarget(a);
		}
		if(target!=null)if(target.isRemoved()||target.hp<=0)target=null;
	}
	public void explode(){
		if(exploded)return;
		exploded=true;
		//Spark.explode(x,y,xv,yv,20,0.03,2,this,true,0.12,true);
		kill();
	}
	public void onKill(){
		explode();
	}
	public double gA(){return 0;}
}
