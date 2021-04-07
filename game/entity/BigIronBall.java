package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class BigIronBall extends IronBall{
private static final long serialVersionUID=1844677L;
	public double width(){return 0.8;}
	public double height(){return 0.8;}
	public double mass(){return 4;}
	public BigIronBall(){
		super();
		hp=800;
	}
	
	@Override
	void touchEnt(Entity ent){
		double k=intersection(ent)*max(0.,v2rel(ent)-0.06)*2000;
		ent.onAttacked(k,this);
		hp-=1;
		super.touchEnt(ent);
	}
	
	void onKill(){
		if(rnd()<0.98)new game.item.BigIronBall().drop(x,y);
		else new game.item.Iron().drop(x,y,rndi(20,30));
	}
	public BmpRes getBmp(){return game.item.IronBall.bmp;}
}
