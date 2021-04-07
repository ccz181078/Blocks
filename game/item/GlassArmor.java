package game.item;

import util.BmpRes;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class GlassArmor extends Armor{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/GlassArmor");
	private static BmpRes bmp_armor=new BmpRes("Armor/GlassArmor");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp_armor;}
	public int maxDamage(){return 500;}
	protected double toolVal(){return 0.6;}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
	public Attack transform(Attack a){
		if(a instanceof NormalAttack)damage+=rf2i(max(0,a.val-1)*10);
		if(a instanceof EnergyAttack)a.val=0;
		if(a instanceof FireAttack)a.val*=0.3;
		return super.transform(a);
	}
}

