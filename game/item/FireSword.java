package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class FireSword extends EnergySwordType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/FireSword");
	public BmpRes getBmp(){return bmp;}
	
	
	@Override
	public void onAttack(Entity e,Source src){
		e.onAttackedByFire(10,src);
		if(rnd()<0.3)Spark.explode(e.x,e.y,e.xv,e.yv,1,0.03,2,src);
		super.onAttack(e,src);
	}
}
