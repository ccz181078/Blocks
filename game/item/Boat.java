package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;

public class Boat extends Shoes{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/Boat");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 30;}
	public int maxDamage(){return 2000;}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}

	@Override
	public Shoes update(Human h){
		h.walk_state=0.5;
		if(h.ydep<0){
			h.xf+=0.3;
			if(h.xdir!=0&&rnd()<0.05)++damage;
		}else if(World.cur.get(h.x,h.y-h.height()+0.3).rootBlock() instanceof game.block.LiquidType){
			h.climbable=true;
			h.ya+=0.01;
			h.xa+=h.xdir*0.01;
			if(rnd()<0.01)++damage;
		}
		return super.update(h);
	}
	
	@Override
	public Attack transform(Attack a){
		if(a instanceof FireAttack)damage+=rf2i(a.val*50);
		return super.transform(a);
	}
};
