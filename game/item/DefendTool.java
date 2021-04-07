package game.item;

import game.entity.*;
import static util.MathUtil.*;

public abstract class DefendTool extends Tool implements AttackFilter,NormalAttacker{
	private static final long serialVersionUID=1844677L;
	protected double toolVal(){return 0;}
	public double armorRate(){return 1-toolVal();}
	public void onDesBlock(game.block.Block b){}
	public void onAttack(Agent Agent){}
	public Attack transform(Attack a){
		damage+=rf2i(a.val);
		a.val*=1-toolVal();
		return a;
	}
	
	public double onImpact(Human h,double v){
		return v;
	}
}
