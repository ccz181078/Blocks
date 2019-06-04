package game.item;

import static util.MathUtil.*;
import game.entity.*;

public class DiamondShield extends IronShield{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 10000;}
	protected double toolVal(){return 0.6;}
	public Attack transform(Attack a){
		if(a instanceof FireAttack)damage+=rf2i(a.val*100);
		return super.transform(a);
	}
}
