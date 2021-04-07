package game.item;

import util.BmpRes;

public class IronArmor extends Armor{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 1000;}
	protected double toolVal(){return 0.6;}
	private static BmpRes bmp=new BmpRes("Item/IronArmor");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(2,4));
		super.onBroken(x,y);
	}
}

