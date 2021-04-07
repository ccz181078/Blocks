package game.entity;

import util.BmpRes;
import static util.MathUtil.*;

public class IronBall extends StoneBall{
private static final long serialVersionUID=1844677L;
	public IronBall(){
		super();
		hp=160;
	}
	void onKill(){
		if(rnd()<0.98)new game.item.IronBall().drop(x,y);
		else new game.item.Iron().drop(x,y,rndi(4,6));
	}
	public BmpRes getBmp(){return game.item.IronBall.bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
}
