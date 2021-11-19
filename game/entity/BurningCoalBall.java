package game.entity;

import util.BmpRes;
import static util.MathUtil.*;

public class BurningCoalBall extends StoneBall{
private static final long serialVersionUID=1844677L;
	public BurningCoalBall(){
		super();
		hp=160;
	}
	
	@Override
	void touchEnt(Entity ent){
		ent.onAttackedByFire(5,this);
		super.touchEnt(ent);
	}
	
	@Override
	void touchBlock(int px,int py,game.block.Block b){
		b.onFireUp(px,py);
	}
	void onKill(){
		Spark.explode(x,y,xv/3,yv/3,25,0.5,12,this,true,0.5);
	}
	public BmpRes getBmp(){return game.item.BurningCoalBall.bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
}
