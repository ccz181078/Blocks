package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class ExplosiveIronNailBall extends IronBall{
private static final long serialVersionUID=1844677L;
	public ExplosiveIronNailBall(){
		super();
		hp=250;
	}
	
	boolean exploded=false;
	void explode(){
		if(exploded)return;
		exploded=true;
		explode(62);
		Spark.explode(x,y,0,0,25,0.1,1,this);
		new game.item.Iron().drop(x,y,rndi(13,16));
		kill();
	}
	
	@Override
	void touchAgent(Agent ent){
		if(rnd()<ent.RPG_ExplodeProb())explode();
		super.touchAgent(ent);
	}

	public double onImpact(double v){
		if(v>2)explode();
		return 0;
	}
	public void onKilled(Source src){
		if(src!=null)explode();
	}
	void onKill(){
		if(!exploded)new game.item.ExplosiveIronNailBall().drop(x,y);
	}
	double getExplodeVel(){return 1.5;}
	public BmpRes getBmp(){return game.item.ExplosiveIronNailBall.bmp;}
	public Entity getBall(){return new Bullet(new game.item.IronNail());}
}
