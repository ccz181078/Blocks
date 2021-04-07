package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class IronNailBall extends IronBall{
private static final long serialVersionUID=1844677L;
	public double mass(){return 0.4;}
	public IronNailBall(){
		super();
		hp=40;
	}
	
	@Override
	void touchEnt(Entity ent){
		double k=intersection(ent)*(v2rel(ent)*10+0.5)*10;
		ent.onAttacked(k,this);
		hp-=1;
		super.touchEnt(ent);
	}
	
	void onKill(){
		if(rnd()<0.98)new game.item.IronNailBall().drop(x,y);
		else new game.item.IronNail().drop(x,y,rndi(8,12));
	}
	public BmpRes getBmp(){return game.item.IronNailBall.bmp;}
}
