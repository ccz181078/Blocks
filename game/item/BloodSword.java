package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class BloodSword extends EnergySwordType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BloodSword");
	public BmpRes getBmp(){return bmp;}
	
	@Override
	public void onAttack(Entity e,Source src){
		if(!(e instanceof Agent))return;
		Agent a=(Agent)e;
		BloodBall.drop(a,rnd(1,3),src);
		super.onAttack(a,src);
	}
	
	public int maxDamage(){return 60;}
	public double repairRate(){return 0.02;}
}

