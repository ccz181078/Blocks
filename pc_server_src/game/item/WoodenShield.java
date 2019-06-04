package game.item;

import static util.MathUtil.*;
import game.entity.*;

public class WoodenShield extends Shield{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 100;}
	protected double toolVal(){return 0.2;}
	public Attack transform(Attack a){
		if(a instanceof FireAttack)damage+=rf2i(a.val*10);
		return super.transform(a);
	}
}
