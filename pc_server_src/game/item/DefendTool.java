package game.item;

import game.entity.*;
import static util.MathUtil.*;

public abstract class DefendTool extends Tool implements AttackFilter{
	protected double toolVal(){return 0;}
	public void onDesBlock(game.block.Block b){}
	public void onAttack(Agent Agent){}
	public Attack transform(Attack a){
		damage+=rf2i(a.val);
		a.val*=1-toolVal();
		return a;
	}
}
