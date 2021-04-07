package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.entity.Agent;
import game.entity.Entity;
import game.entity.Player;
import util.BmpRes;
import java.util.ArrayList;

public class ProtectionZone extends Buff{
	private static final long serialVersionUID=1844677L;
	@Override
	public boolean harmless(){return true;}
	@Override
	public double gA(){return 0;}
	@Override
	public double light(){return 1;}
	
	
	double radius=10;
	int t=0;

	@Override
	public boolean chkAgent(){
		return false;
	}
	
	public ProtectionZone(double x,double y,double radius,Agent src,int time){
		super(src);
		this.x=x;
		this.y=y;
		this.radius=radius;
		xv=yv=0;
		hp=time;
	}
	
	public static void setPlayer(Agent pl){
		new ProtectionZone(pl.x,pl.y,4,pl,300).add();
	}
	
	@Override
	public void update0(){
		super.update0();
		shadowed=false;
	}
	
	protected void ejectEnt(Entity e){
		double xd=e.x-x,yd=e.y-y,d=hypot(xd,yd)+1e-8;
		if(d>radius)return;
		e.xa+=xd/d*0.3;
		e.ya+=yd/d*0.3;
		e.fc+=5;
	}
	protected void ejectAgent(Agent e){
		ejectEnt(e);
	}
	
	@Override
	public void update(){
		if(target==null){
			hp=0;
			return;
		}
		hp-=1;
		if(t>0){
			game.world.NearbyInfo ni=World.cur.getNearby(x,y,radius,radius,false,true,true);
			for(Entity e:ni.ents){
				if(!e.harmless())ejectEnt(e);
			}
			for(Agent e:ni.agents){
				if(e!=target)ejectAgent(e);
			}
			target.addHp(target.maxHp());
			target.fc+=0.1;
		}
		++t;
		xv=yv=0;
		new SetRelPos(this,target,0,0);
	}

	@Override
	public void draw(Canvas cv){
		if(t==0)return;
		cv.rotate(World.cur.time%360);
		float r=(float)radius;
		float ps[]=new float[32];
		for(int i=0;i<8;++i){
			float c=(float)cos(PI/4*i),s=(float)sin(PI/4*i);
			ps[i*4+0]=c*r;
			ps[i*4+1]=s*r;
			c=(float)cos(PI/4*(i+0.2));
			s=(float)sin(PI/4*(i+0.2));
			ps[i*4+2]=c*r;
			ps[i*4+3]=s*r;
		}
		cv.drawLines(ps,0xff00ff00);
	}
	
	
}
