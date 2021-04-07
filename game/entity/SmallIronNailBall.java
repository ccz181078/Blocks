package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class SmallIronNailBall extends IronBall{
private static final long serialVersionUID=1844677L;
	public double mass(){return 0.4;}
	public double width(){return 0.3;}
	public double height(){return 0.3;}
	public SmallIronNailBall(){
		super();
		hp=1000;
	}
	
	@Override
	void touchEnt(Entity ent){
		double k=intersection(ent)*(v2rel(ent)*10+1)*3;
		ent.onAttacked(k,this);
		if(ent instanceof Agent)exchangeVel(ent,0.7);
		hp-=1;
		super.touchEnt(ent);
	}
	
	void onKill(){
		if(rnd()<0.9)new game.item.SmallIronNailBall().drop(x,y);
		else new game.item.IronNail().drop(x,y,rndi(8,12));
	}
	public BmpRes getBmp(){return game.item.SmallIronNailBall.bmp;}
	public double touchVal(){return 0.3;}
}
