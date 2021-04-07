package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class HeatBall extends IronBall{
private static final long serialVersionUID=1844677L;
	public HeatBall(){
		super();
		hp=1000;
	}
	public double mass(){return 1;}
	
	@Override
	void touchEnt(Entity ent){
		ent.onAttackedByFire(100,this);
		exchangeVel(ent,0.3);
		hp-=200;
		super.touchEnt(ent);
	}
	
	@Override
	public double gA(){return 0.03;}
	
	@Override
	public void update(){
		super.update();
	}
	
	@Override
	void touchBlock(int px,int py,game.block.Block b){
		b.onFireUp(px,py);
	}

	void onKill(){
		ShockWave.explode(x,y,xv,yv,40,0.2,4,this);//2560
		Spark.explode(x,y,xv,yv,32,0.05,1,this,true,0.4);
	}
	public BmpRes getBmp(){return game.item.HeatBall.bmp;}
}
