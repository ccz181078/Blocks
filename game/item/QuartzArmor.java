package game.item;

import util.BmpRes;
import game.entity.Attack;
import game.entity.FireAttack;
import static util.MathUtil.*;

public class QuartzArmor extends IronArmor{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/QuartzArmor");
	private static BmpRes bmp_armor=new BmpRes("Armor/QuartzArmor");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp_armor;}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
	public Attack transform(Attack a){
		if(a instanceof FireAttack)a.val*=0.3;
		return super.transform(a);
	}
}

