package game.item;

import util.BmpRes;
import static util.MathUtil.rnd;

public class CactusHammer extends HammerType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/CactusHammer");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 1;}
	public int swordVal(){return 3;}
	public int maxDamage(){return 500;}
	public int fuelVal(){return 30;}
	public void onBroken(double x,double y){
		if(rnd()<0.8)new Stick().drop(x,y);
	}
}

