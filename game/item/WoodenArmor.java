package game.item;

import util.BmpRes;
import game.entity.Attack;
import game.entity.FireAttack;
import static util.MathUtil.*;

public class WoodenArmor extends Armor{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 100;}
	protected double toolVal(){return 0.3;}
	private static BmpRes bmp=new BmpRes("Item/WoodenArmor");
	private static BmpRes bmp_armor=new BmpRes("Armor/WoodenArmor");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp_armor;}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
	public Attack transform(Attack a){
		if(a instanceof FireAttack)damage+=rf2i(a.val*10);
		return super.transform(a);
	}
}

