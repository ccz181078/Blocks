package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;

public class Mace extends HammerType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Mace");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 1;}
	public int swordVal(){return 4;}
	public int maxDamage(){return 2000;}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,rndi(2,3));
	}
}

