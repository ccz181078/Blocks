package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;

public class SkateBoard extends Shoes{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/SkateBoard");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	public int maxDamage(){return 5000;}
	//public double friction(){return 0.3;}

	@Override
	public Shoes update(Human h){
		h.walk_state=0.5;
		if(h.ydep<0&&h.xdir!=0){
			h.impulse(h.xdir,0,0.01);
			h.xa+=h.xdir*0.01;
			if(h.xdir!=0&&rnd()<0.01)++damage;
		}
		return super.update(h);
	}

	@Override
	public Attack transform(Attack a){
		return super.transform(a);
	}
};
