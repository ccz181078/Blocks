package game.item;

import game.block.Block;
import game.entity.*;
import static util.MathUtil.*;
import util.BmpRes;

public abstract class Armor extends Tool implements AttackFilter{
	private static final long serialVersionUID=1844677L;
	protected double toolVal(){return 0;}
	public void onDesBlock(Block b){}
	public void onAttack(Agent Agent){}
	public abstract BmpRes getArmorBmp();
	public Attack transform(Attack a){
		damage+=rf2i(a.val);
		a.val*=1-toolVal();
		return a;
	}
}
