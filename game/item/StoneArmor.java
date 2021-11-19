package game.item;

import util.BmpRes;
import game.entity.Attack;
import game.entity.FireAttack;
import static util.MathUtil.*;

public class StoneArmor extends Armor{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 200;}
	protected double toolVal(){return 0.5;}
	private static BmpRes bmp=new BmpRes("Item/StoneArmor");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
}

