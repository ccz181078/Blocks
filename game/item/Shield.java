package game.item;

import game.entity.*;
import static util.MathUtil.*;

public abstract class Shield extends DefendTool{
	private static final long serialVersionUID=1844677L;
	public Attack transform(Attack a){
		if(a instanceof NormalAttack){
			a.val*=0.7+0.3*((NormalAttack)a).getWeight(this);
		}
		return super.transform(a);
	}
}
