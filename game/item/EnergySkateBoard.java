package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;

public class EnergySkateBoard extends EnergyShoes{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/EnergySkateBoard");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	public int maxDamage(){return 5000;}
	//public double friction(){return 0.3;}
	@Override
	public double getJumpAcc(Human h,double v){
		damage+=1;
		if(hasEnergy(1)){
			loseEnergy(1);
			return v;
		}
		return v*0.7;
	}

	@Override
	public Shoes update(Human h){
		if(hasEnergy(1)){
			h.walk_state=0.5;
			if(h.ydep<0){
				h.impulse(h.xdir,0,0.03);
				if(h.xdir!=0&&rnd()<0.01)++damage;
				if(h.xdir!=0&&rnd()<0.1)loseEnergy(1);
			}
		}
		return super.update(h);
	}
};
