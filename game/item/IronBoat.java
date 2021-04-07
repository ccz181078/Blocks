package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;

public class IronBoat extends Shoes{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/IronBoat");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 4000;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}

	@Override
	public Shoes update(Human h){
		if(h.ydep<0){
			h.xf+=0.3;
			if(h.xdir!=0&&rnd()<0.05)++damage;
		}else if(World.cur.get(h.x,h.y-h.height()+0.3).rootBlock() instanceof game.block.LiquidType
			&& !(World.cur.get(h.x,h.y-h.height()+1).rootBlock() instanceof game.block.LiquidType)){
			h.walk_state=0.3;
			h.climbable=true;
			h.ya+=0.005;
			h.xa+=h.xdir*0.01;
			if(rnd()<0.01)++damage;
		}
		return super.update(h);
	}
};
