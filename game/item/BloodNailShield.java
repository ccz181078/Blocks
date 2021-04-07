package game.item;

import static util.MathUtil.*;
import game.entity.*;

public class BloodNailShield extends IronShield{
	private static final long serialVersionUID=1844677L;
	public int swordVal(){return 4;}
	public void onAttack(Entity e,Source src){
		if(!(e instanceof Agent))return;
		Agent a=(Agent)e;
		if(rnd()<0.1)BloodBall.drop(a,rnd(1,3),src);
		super.onAttack(a,src);
	}
}