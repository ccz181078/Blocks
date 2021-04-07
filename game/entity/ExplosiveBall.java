package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class ExplosiveBall extends IronBall{
private static final long serialVersionUID=1844677L;
	public ExplosiveBall(){
		super();
		hp=250;
	}
	
	boolean exploded=false;
	void explode(){
		if(exploded)return;
		exploded=true;
		explode(150);
		Spark.explode(x,y,0,0,20,0.1,1.5,this);
		ShockWave.explode(x,y,0,0,40,0.1,0.6,this);
		new game.item.Iron().drop(x,y,rndi(13,16));
		kill();
	}
	
	@Override
	void touchAgent(Agent ent){
		if(rnd()<ent.RPG_ExplodeProb())explode();
		super.touchAgent(ent);
	}

	public double onImpact(double v){
		if(v>0.7)explode();
		return 0;
	}
	public void onKilled(Source src){
		if(src!=null)explode();
	}
	void onKill(){
		if(!exploded)new game.item.ExplosiveBall().drop(x,y);
	}
	public BmpRes getBmp(){return game.item.ExplosiveBall.bmp;}
}
