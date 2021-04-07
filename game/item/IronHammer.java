package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;

public class IronHammer extends HammerType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/IronHammer");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 4;}
	public int maxDamage(){return 25000;}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,rndi(4,6));
		super.onBroken(x,y);
	}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
}

