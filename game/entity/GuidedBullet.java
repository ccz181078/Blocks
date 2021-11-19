package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;
import game.item.Armor;
import game.item.GuidedBulletManager;

public class GuidedBullet extends Bullet{
	private static final long serialVersionUID=1844677L;
	public double width(){return hp<120?0.1:0.2;}
	public double height(){return hp<120?0.1:0.2;}
	public double mass(){return 0.15;}
	public boolean chkRigidBody(){return bullet.chkBlock();}
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
	protected final boolean checkRecycle(Entity ent){
		if(!(ent instanceof Human))return false;
		if(ent!=source.getSrc())return false;
		Human h=(Human)ent;
		Armor ar=h.armor.get();
		if(ar instanceof GuidedBulletManager){
			if(exploded||hp>120)return true;
			exploded=true;
			kill();
			bullet.drop(h.x,h.y);
			return true;
		}
		return false;
	}
	@Override
	void touchEnt(Entity ent){
		if(source==ent.source&&(ent instanceof GuidedBullet))return;
		if(checkRecycle(ent))return;
		explode();
	}
	double near_cnt=1;
	public void update(){
		super.update();
		boolean go_back=false;
		if(hp<120){
			Agent a=source.getSrc();
			if(a instanceof Human){
				Human h=(Human)a;
				Armor ar=h.armor.get();
				if(ar instanceof GuidedBulletManager){
					target=a;
					go_back=true;
				}
			}
		}
		fc+=0.02/mass();
		game.world.NearbyInfo ni=World.cur.getNearby(x,y,4,4,false,true,true);
		double xvs=xv,yvs=yv,s=1;
		double xds=xv+rnd_gaussion()*0.1,yds=yv+rnd_gaussion()*0.1;
		for(Entity e:ni.ents){
			if(e.harmless())continue;
			double xd=x-e.x,yd=y-e.y,d=1/(hypot(xd,yd)+1e-2);
			if(d<1/4.)continue;
			if(e instanceof GuidedBullet){
				if(!go_back){
					xvs+=e.xv;
					yvs+=e.yv;
					s+=1;
					double c=d*d*(d*(1.5+(near_cnt-1)*0.15)-1);
					xds+=xd*c;
					yds+=yd*c;
					Agent tg=((GuidedBullet)e).target;
					if(tg!=null){
						updateTarget(tg);
					}
				}
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
		else{
			if(x<World.cur.getMinX())xds+=1;
			if(x>World.cur.getMaxX())xds-=1;
			if(y<World.cur.getMinY())yds+=1;
			if(y>World.cur.getMaxY())yds-=1;
		}
		if(!go_back)
		for(Agent a:ni.agents){
			Agent b=a;
			if(a instanceof Airship_Flank){
				Airship_Flank af=(Airship_Flank)a;
				if(af.airship!=null)b=af.airship;
			}
			if(b==source.getSrc())continue;
			updateTarget(a);
		}
		if(!go_back&&target==source.getSrc()){
			target=null;
		}
		if(target!=null){
			double xd=x-target.x,yd=y-target.y,d=1/(hypot(xd,yd)+1e-2),c=-d*4;
			xds+=xd*c;
			yds+=yd*c;
			if(!go_back){
				xds+=yd*c*c*0.1;
				yds-=xd*c*c*0.1;
			}
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
