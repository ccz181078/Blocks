package game.item;

import static util.MathUtil.*;

public class IronShield extends Shield{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 1000;}
	protected double toolVal(){return 0.4;}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,rndi(1,3));
	}
}
