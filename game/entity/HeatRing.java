package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class HeatRing extends IronBall{
private static final long serialVersionUID=1844677L;
	public HeatRing(){
		super();
		hp=200;
	}
	public double mass(){return 0.5;}
	transient boolean touched=false;
	
	int time=4000;
	
	@Override
	void touchEnt(Entity ent){
		ent.onAttackedByFire(10,this);
		exchangeVel(ent,0.3);
		touched=true;
		super.touchEnt(ent);
	}
	
	@Override
	public double gA(){return touched?0.001:0.03;}
	
	@Override
	public void update(){
		touched=false;
		super.update();
		if(touched&&time>0){
			int c=max(1,time/400);
			time-=c;
			Spark.explode(x,y,xv,yv,1,0.05,0.1*c,this,true,0.4);
			hp-=0.1;
		}
		if(time<=0)kill();
	}
	@Override
	void touchBlock(int px,int py,game.block.Block b){
		b.onFireUp(px,py);
		if(!b.isCoverable()){
			touched=true;
			hp-=0.3;
			b.des(px,py,1,this);
		}
	}

	void onKill(){
		ShockWave.explode(x,y,0,0,40,0.1,2,this);
		explode(30);
	}
	public BmpRes getBmp(){return game.item.HeatRing.bmp;}
}
