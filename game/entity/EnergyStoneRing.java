package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class EnergyStoneRing extends IronBall{
private static final long serialVersionUID=1844677L;
	public EnergyStoneRing(){
		super();
		hp=200;
	}
	public double mass(){return 0.5;}
	transient boolean touched=false;
	
	@Override
	void touchEnt(Entity ent){
		ent.onAttackedByEnergy(10,this);
		exchangeVel(ent,0.3);
		touched=true;
		super.touchEnt(ent);
	}
	
	@Override
	public double gA(){return touched?-0.001:-0.03;}
	
	@Override
	public void update(){
		touched=false;
		super.update();
		if(touched&&rnd()<0.1)kill();
	}
	@Override
	void touchBlock(int px,int py,game.block.Block b){
		if(!b.isCoverable()){
			touched=true;
			hp-=0.3;
			b.des(px,py,1,this);
		}
	}

	void onKill(){
		explode(30);
	}
	public Entity getBall(){return new EnergyBall().setHpScale(10);}
	public BmpRes getBmp(){return game.item.EnergyStoneRing.bmp;}
}
