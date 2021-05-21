package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class GuidedBullet extends Bullet{
	private static final long serialVersionUID=1844677L;
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return 0.15;}
	public boolean chkRigidBody(){return false;}
	public boolean rotByVelDir(){return true;}
	boolean exploded=false;
	
	public BmpRes getBmp(){return bullet.getBmp();}
	
	public GuidedBullet(game.item.GuidedBullet b){
		super(b);
		hp=200;
	}
	Agent target=null;
	private void updateTarget(Agent w){
		if(!w.hasBlood())return;
		if(target==null)target=w;
		else if(distL2(w)<distL2(target))target=w;
	}
	@Override
	void touchEnt(Entity ent){
		if(source==ent.source&&(ent instanceof GuidedBullet))return;
		explode();
	}
	double near_cnt=1;
	public void update(){
		super.update();
		fc+=0.02/mass();
		game.world.NearbyInfo ni=World.cur.getNearby(x,y,4,4,false,true,true);
		double xvs=xv,yvs=yv,s=1;
		double xds=xv+rnd_gaussion()*0.1,yds=yv+rnd_gaussion()*0.1;
		for(Entity e:ni.ents){
			if(e.harmless())continue;
			double xd=x-e.x,yd=y-e.y,d=1/(hypot(xd,yd)+1e-2);
			if(d<1/4.)continue;
			if(e instanceof GuidedBullet){
				xvs+=e.xv;
				yvs+=e.yv;
				s+=1;
				double c=d*d*(d*(1.5+(near_cnt-1)*0.15)-1);
				xds+=xd*c;
				yds+=yd*c;
				Agent tg=((GuidedBullet)e).target;
				if(tg!=null)updateTarget(tg);
			}else{
				double c=d*d;
				xds+=xd*c;
				yds+=yd*c;
			}
		}
		near_cnt=s;
		if(bullet.chkBlock())
		for(int i=0;i<3;++i){
			double xd=rnd_gaussion()*2,yd=rnd_gaussion()*2;
			if(World.cur.get(x+xd,y+yd).isSolid()){
				xds-=xd*2;
				yds-=yd*2;
				fc+=0.03/mass();
			}
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
		if(target!=null){
			double xd=x-target.x,yd=y-target.y,d=1/(hypot(xd,yd)+1e-2),c=-d*4;
			xds+=xd*c;
			yds+=yd*c;
		}
		xds+=(xvs-xv*s);
		yds+=(yvs-yv*s);
		double d=1/(hypot(xds,yds)+1e-2);
		xds*=d;
		yds*=d;
		xa+=xds*0.03;
		ya+=yds*0.03;
		if(target!=null)if(target.isRemoved()||target.hp<=0)target=null;
	}
	public void explode(){
		if(exploded)return;
		exploded=true;
		Spark.explode(x,y,xv,yv,20,0.03,2,this,true,0.12,true);
		kill();
	}
	public void onKill(){
		explode();
	}
	public double gA(){return 0;}
}
