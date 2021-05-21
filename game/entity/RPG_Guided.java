package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_Guided extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_Guided rpg;
	Entity target;
	public double width(){return 0.3;}
	public double height(){return 0.3;}
	public double mass(){return 0.8;}
	public BmpRes getBmp(){return rpg.getBmp();}
	public boolean chkRigidBody(){return true;}
	public RPG_Guided(game.item.RPG_Guided a){
		super();
		rpg=a;
		fuel*=4;
		hp*=10;
	}
	void touchBlock(int px,int py,Block b){
		if(fuel>0){
			b.onFireUp(px,py);
			if(b.isSolid()){
				if(hp>300){
					b.des(px,py,100,this);
					hp-=300;
				}else{
					try_explode();
				}
			}
		}else if(b.isSolid()){
			try_explode();
		}
	}
	void touchEnt(Entity ent){
		if(ent==target){
			try_explode();
		}else{
			if(hp>200&&!( ent instanceof RPG_Guided )){
				hp-=200;
			}else{
				try_explode();
			}
		}
	}
	public void update(){
		super.update();
		double v=sqrt(xv*xv+yv*yv+1e-8);
		f+=0.3*v*v;
		if(fuel<=0)return;
		if(target==null&&rnd()<0.2){
			double r=12,c0=-1;
			double x1=x+xv/v*r,y1=y+yv/v*r;
			for(Agent a:World.cur.getNearby(x1,y1,r,r,false,false,true).agents){
				if(a instanceof Airship_Flank){
					Airship_Flank af=(Airship_Flank)a;
					if(af.airship!=null)a=af.airship;
				}
				if(a==source.getSrc()||!a.hasBlood())continue;
				double xd=a.x-x,yd=a.y-y;
				double c=(xd*xv+yd*yv)/sqrt(xd*xd+yd*yd+1e-8);
				if(c>c0){c0=c;target=a;}
			}
		}
		if(target!=null&&target.isRemoved())target=null;
		if(target!=null){
			double xd=target.x-x,yd=target.y-y;
			double d=xd*yv-yd*xv;
			double k=0.03*(d>0?1:-1);
			aa+=(ax*yd-ay*xd>0?1:-1)*0.03-av*0.2;
			//xa+=yv/v*k;
			//ya+=-xv/v*k;
			d=distL2(target);
			//if(d<5&&xd*xv+yd*yv<0)try_explode();
		}
	}
	@Override
	public void test_update(){
		super.test_update();
		double v=sqrt(xv*xv+yv*yv);
		f+=0.3*v*v;
	}
	public void explode(){
		Spark.explode_adhesive(x,y,xv,yv,fuel/16,0.1,4,this);
		kill();
	}
}
