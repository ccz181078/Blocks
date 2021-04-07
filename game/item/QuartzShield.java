package game.item;

import static util.MathUtil.*;
import game.entity.*;

public class QuartzShield extends IronShield{
	private static final long serialVersionUID=1844677L;
	public Attack transform(Attack a){
		if(a instanceof FireAttack)a.val*=0.3;
		return super.transform(a);
	}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
}
