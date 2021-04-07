package game.item;

import util.BmpRes;
import game.entity.Attack;
import game.entity.FireAttack;
import static util.MathUtil.*;

public class DiamondArmor extends IronArmor{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 10000;}
	protected double toolVal(){return 0.85;}
	private static BmpRes bmp=new BmpRes("Item/DiamondArmor");
	private static BmpRes bmp_armor=new BmpRes("Armor/DiamondArmor");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp_armor;}
	public double hardness(){return game.entity.NormalAttacker.DIAMOND;}
	public Attack transform(Attack a){
		if(a instanceof FireAttack)damage+=rf2i(a.val*100);
		return super.transform(a);
	}
}

