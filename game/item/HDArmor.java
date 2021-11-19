package game.item;

import util.BmpRes;
import game.entity.Attack;
import game.entity.NormalAttack;
import static util.MathUtil.*;

public class HDArmor extends IronArmor{
	public int maxDamage(){return 4000;}
	protected double toolVal(){return 0.8;}
	private static BmpRes bmp=new BmpRes("Item/HDArmor");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	public Attack transform(Attack a){
		if(a instanceof NormalAttack){
			a.val*=((NormalAttack)a).getWeight(this);
		}
		damage+=rf2i(a.val);
		return a;
	}
}

