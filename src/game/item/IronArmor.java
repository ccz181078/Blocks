package game.item;

import util.BmpRes;

public class IronArmor extends Armor{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 1000;}
	protected double toolVal(){return 0.6;}
	private static BmpRes bmp=new BmpRes("Item/IronArmor");
	private static BmpRes bmp_armor=new BmpRes("Armor/IronArmor");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp_armor;}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(2,4));
	}
}

