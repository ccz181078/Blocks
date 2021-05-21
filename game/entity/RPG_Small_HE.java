package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

abstract class RPG_Small_Guided extends RPG_Guided{
	public BmpRes getBmp(){return rpg.getBmp();}
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return 0.2;}
	
	public boolean chkRigidBody(){return true;}
	public boolean chkBlock(){return true;}
	
	RPG_Small_Guided(game.item.RPG_Guided a){
		super(a);
	}
	
	@Override
	double _fc(){return 0.001;}
	
	@Override
	public void update(){
		super.update();
		double v=sqrt(xv*xv+yv*yv+1e-8);
		if(fuel<=0)return;
		if(target==null&&rnd()<0.2){
			double r=12,c0=-1;
			double x1=x+xv/v*r,y1=y+yv/v*r;
			for(Agent a:World.cur.getNearby(x1,y1,r,r,false,false,true).agents){
				if(a instanceof Airship_Flank){
					Airship_Flank af=(Airship_Flank)a;
					if(af.airship!=null)a=af.airship;
				}
				if(a==source.getSrc())continue;
				double xd=a.x-x,yd=a.y-y;
				double c=(xd*xv+yd*yv)/sqrt(xd*xd+yd*yd+1e-8);
				if(c>c0){c0=c;target=a;}
			}
		}
		if(target!=null&&target.isRemoved())target=null;
		if(target!=null){
			double xd=target.x-x,yd=target.y-y;
			yd+=min(8,abs(xd))*0.2;
			double d=xd*yv-yd*xv;
			double k=0.03*(d>0?1:-1);
			aa+=(ax*yd-ay*xd>0?1:-1)*0.3-av*0.05;
			//xa+=yv/v*k;
			//ya+=-xv/v*k;
			d=distL2(target);
			//if(d<5&&xd*xv+yd*yv<0)try_explode();
		}
	}
	protected boolean genSmoke(){return rnd()*3<hypot(xv,yv);}
	@Override
	void touchBlock(int px,int py,Block b){
		if(fuel>0){
			b.onFireUp(px,py);
			if(b.isSolid())b.des(px,py,3,this);
		}
		else if(b.isSolid())
			kill();
	}
	@Override
	public void onKill(){
		try_explode();
		Fragment.gen(x,y,width(),height(),2,2,4,getBmp());
	}
	public Attack transform(Attack a){
		if(a!=null)a.val*=0.1;
		return a;
	}
}

public class RPG_Small_HE extends RPG_Small_Guided{
	public RPG_Small_HE(game.item.RPG_Small_HE a){
		super(a);
	}
	@Override
	void touchEnt(Entity ent){
		if( ent == target && rnd() < 0.1 ) kill();
		hp -= 3;
	}
	public void explode(){
		Spark.explode_adhesive(x,y,xv,yv,fuel/16,0.1,4,this);
		explode(50);
		Spark.explode(x,y,0,0,10,0.1,1,this);
		ShockWave.explode(x,y,0,0,20,0.1,0.4,this);
		kill();
	}
}
