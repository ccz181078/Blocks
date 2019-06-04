package game.item;

import static util.MathUtil.*;
import util.BmpRes;

public class IronPickax extends PickaxType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/IronPickax");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 2;}
	public int maxDamage(){return 5000;}
	public void onBroken(double x,double y){
		if(rnd()<0.5)new Stick().drop(x,y);
		new Iron().drop(x,y,rndi(1,2));
	}
}

